package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.annotation.DataBackUp;
import com.tyut.constant.*;
import com.tyut.context.BaseContext;
import com.tyut.context.UserContext;
import com.tyut.dto.AppointmentQueryDTO;
import com.tyut.dto.ExactTimeAppointmentDTO;
import com.tyut.entity.Appointment;
import com.tyut.entity.DoctorSchedule;
import com.tyut.exception.BaseException;
import com.tyut.mapper.AppointmentMapper;
import com.tyut.mapper.DoctorScheduleMapper;
import com.tyut.result.PageResult;
import com.tyut.service.AppointmentService;
import com.tyut.utils.AppointmentUtil;
import com.tyut.utils.QueueManagerUtil;
import com.tyut.utils.TriageUtil;
import com.tyut.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;
    @Autowired
    private TriageUtil triageUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 前端需要展示：
     * 用户根据doctorQueryDTO中的内容查询到的doctorDetailVO
     * doctorDetailVO中包含doctorSchedule的List
     * 从众多医生满足条件的doctorSchedule中，患者选择一个自己方便的时段（例如 周一上午）
     * 患者选择时段完毕后，需要定位一个精确日期
     * 如果患者选择的星期数大于等于今日的星期数，提示患者选择的日期为本周该时段的具体日期（如2026-10-1）
     * 如果患者选择的星期数小于今日的星期数，则提示患者选择的日期为下周该时段的具体日期（如2026-10-8）
     * 如果患者选择的是上午的时段，可供选择的时间点为：8:30、9:00、9:30、10:00、10:30、11:00、11:30
     * 如果患者选择的是下午的时段，可供选择的时间点为：13:00、13:30、14:00、14:30、15:00、15:30、16:00、16:30、17:00
     * 星期数和具体日期需要完整对应，通过前段定义computed 属性，将星期数和具体日期对应起来
     *
     * @param dto
     */
    @DataBackUp(module = ModuleConstant.APPOINTMENT)
    @Override
    public void saveAppointment(ExactTimeAppointmentDTO dto) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(dto, appointment);
        appointment.setAppointmentStatus(AppointConstant.BOOKED);
        appointment.setVisitStatus(VisitConstant.WAITING);
        appointment.setCreateTime(LocalDateTime.now());
        DoctorSchedule doctorSchedule = doctorScheduleMapper.selectById(dto.getScheduleId());
        if (doctorSchedule == null) throw new BaseException("当前医生排班不存在");
        if (doctorSchedule.getStatus() == 0) throw new BaseException("该医生已停诊");
        if (doctorSchedule.getCurrentNumber() >= doctorSchedule.getMaxNumber())
            throw new BaseException("当前医生已满号");
        AppointmentQueryDTO query= new AppointmentQueryDTO();
        BeanUtils.copyProperties(dto, query);
        Integer weekDay = doctorSchedule.getWeekDay();
        Integer triageLevel = triageUtil.determineTriageLevel(appointment.getSymptom());
        appointment.setTriageLevel(triageLevel);
        String queueNo = AppointmentUtil.generateQueueNo(weekDay, doctorSchedule.getTimeSlot()
                , dto.getDoctorId(), dto.getResidentId());
        appointment.setQueueNo(queueNo);
        appointment.setIsDeleted(AccountConstant.NOT_DELETE);
        appointmentMapper.insert(appointment);

        //发送通知给医生
        sendNotification("doctor", dto.getDoctorId(), "新预约通知", appointment.getQueueNo());
    }

    @Override
    public PageResult list(AppointmentQueryDTO dto) {
        Page<Appointment> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        if (dto.getDoctorId() != null) {
            queryWrapper.eq(Appointment::getDoctorId, dto.getDoctorId());
        }
        if (dto.getResidentId() != null) {
            queryWrapper.eq(Appointment::getResidentId, dto.getResidentId());
        }
        if (dto.getAppointmentStatus() != null) {
            queryWrapper.eq(Appointment::getAppointmentStatus, dto.getAppointmentStatus());
        }
        if (dto.getVisitStatus() != null) {
            queryWrapper.eq(Appointment::getVisitStatus, dto.getVisitStatus());
        }
        if (dto.getAppointmentDate() != null) {
            queryWrapper.eq(Appointment::getAppointmentDate, dto.getAppointmentDate());
        }
        if(dto.getAppointmentTime()!=null){
            queryWrapper.eq(Appointment::getAppointmentTime, dto.getAppointmentTime());
        }
        // 根据用户角色决定排序策略
        Integer currentUserRole = BaseContext.getCurrentRole();

        // 如果是居民用户(角色为2)，按照预约日期和时间降序排序
        if (currentUserRole != null && currentUserRole == AccountConstant.ROLE_RESIDENT) {
            queryWrapper.orderByDesc(Appointment::getAppointmentDate)
                    .orderByDesc(Appointment::getAppointmentTime);
        }
        // 如果是医生用户或其他情况，保持原有逻辑
        else {
            // 按照创建时间降序排列，确保最新的预约在前面
            queryWrapper.orderByDesc(Appointment::getCreateTime);
        }
        IPage<Appointment> appointmentIPage = appointmentMapper.selectPage(page, queryWrapper);
        List<Appointment> appointments = appointmentIPage.getRecords();
        if (shouldApplyQueueSorting(dto)) {
            List<QueueManagerUtil.WaitingPatient> waitingPatients = appointments.stream()
                    .map(QueueManagerUtil.WaitingPatient::new)
                    .collect(Collectors.toList());

            List<Appointment> sortedAppointments = QueueManagerUtil.getCallOrder(waitingPatients);
            appointmentIPage.setRecords(sortedAppointments);
        }
        return PageResult.builder()
                .total(appointmentIPage.getTotal())
                .dataList(appointmentIPage.getRecords())
                .build();
    }
    @DataBackUp(module = ModuleConstant.APPOINTMENT)
    @Override
    public void cancelAppointment(Long appointmentId, String cancelReason) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        //预约状态和就诊状态必须为预约中且未开始
        if (!Objects.equals(appointment.getAppointmentStatus(), AppointConstant.BOOKED)
                || !Objects.equals(appointment.getVisitStatus(), VisitConstant.WAITING)
        ) {
            throw new BaseException("当前预约不可取消！");
        }
        appointment.setAppointmentStatus(AppointConstant.CANCELLED);
        appointment.setCancelReason(cancelReason);
        appointment.setCancelTime(LocalDateTime.now());
        appointmentMapper.updateById(appointment);

        //发送通知给医生
        sendNotification("doctor", appointment.getDoctorId(), "取消预约通知", appointment.getQueueNo());
    }
    @DataBackUp(module = ModuleConstant.APPOINTMENT)
    @Override
    public void call(Long appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (!Objects.equals(appointment.getAppointmentStatus(), AppointConstant.BOOKED)
                || Objects.equals(appointment.getVisitStatus(), VisitConstant.IN_VISIT))
        {
            throw new BaseException("当前预约不可叫号！");
        }
        appointment.setVisitStatus(VisitConstant.CALLED);

        appointmentMapper.updateById(appointment);
        sendNotification("resident", appointment.getResidentId(), "叫号通知", appointment.getQueueNo());
    }
    @DataBackUp(module = ModuleConstant.APPOINTMENT)
    @Override
    public void startConsult(Long appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if(appointment.getAppointmentStatus() != AppointConstant.BOOKED){
            throw new BaseException("当前预约不可开始咨询！");
        }
        if(appointment.getVisitStatus() != VisitConstant.CALLED){
            throw new BaseException("请先叫号！");
        }
        appointment.setVisitStatus(VisitConstant.IN_VISIT);
        appointmentMapper.updateById(appointment);
    }
    @DataBackUp(module = ModuleConstant.APPOINTMENT)
    @Override
    public void skip(Long appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if(!Objects.equals(appointment.getAppointmentStatus(), AppointConstant.BOOKED)
                || !Objects.equals(appointment.getVisitStatus(), VisitConstant.CALLED)
        ){
            throw new BaseException("当前预约不可跳过！");
        }
        appointment.setVisitStatus(VisitConstant.SKIPPED);
        appointmentMapper.updateById(appointment);
        sendNotification("resident", appointment.getResidentId(), "过号通知", appointment.getQueueNo());
    }
    @DataBackUp(module = ModuleConstant.APPOINTMENT)
    @Override
    public void finish(Long appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if(!Objects.equals(appointment.getAppointmentStatus(), AppointConstant.BOOKED)
                || !Objects.equals(appointment.getVisitStatus(), VisitConstant.IN_VISIT)
        ){
            throw new BaseException("当前预约不可结束！");
        }
        appointment.setAppointmentStatus(AppointConstant.FINSHED);
        appointment.setVisitStatus(VisitConstant.DONE);
        appointmentMapper.updateById(appointment);
        sendNotification("resident", appointment.getResidentId(), "结束咨询通知", appointment.getQueueNo());
    }
    @Override
    public Boolean isAppointed(AppointmentQueryDTO dto) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getResidentId, dto.getResidentId())
                .eq(Appointment::getDoctorId,dto.getDoctorId())
                .eq(Appointment::getAppointmentDate,dto.getAppointmentDate())
                .eq(Appointment::getAppointmentTime,dto.getAppointmentTime());
        Appointment appointment = appointmentMapper.selectOne(queryWrapper);
        return appointment != null;
    }

    /**
     * 判断是否需要应用队列排序
     */
    private boolean shouldApplyQueueSorting(AppointmentQueryDTO dto) {
        // 当查询条件包含医生ID且访问状态为等待中或已叫号时，应用队列排序
        return dto.getDoctorId() != null &&
                (dto.getVisitStatus() == null ||
                        dto.getVisitStatus() == VisitConstant.WAITING ||
                        dto.getVisitStatus() == VisitConstant.CALLED);
    }

    

    /**
     * 通用通知发送方法
     *
     * @param userType 用户类型：doctor/resident
     * @param userId   用户ID
     * @param title    通知标题
     * @param queueNo  排队序号
     */
    private void sendNotification(String userType, Long userId, String title, String queueNo) {
        try {
            String sid = userType + "_" + userId;
            String message = title + "：请关注您的排队序号 " + queueNo;

            if (webSocketServer.isUserOnline(userType + "_", userId)) {
                webSocketServer.sendToClient(sid, buildNotificationJson(title, queueNo));
            }
        } catch (Exception e) {
            log.error("发送通知失败", e);
        }
    }

    /**
     * 构建简化通知JSON格式
     */
    private String buildNotificationJson(String title, String queueNo) {
        return String.format(
                "{"
                        + "\"type\":\"notification\","
                        + "\"title\":\"%s\","
                        + "\"queueNo\":\"%s\","
                        + "\"timestamp\":\"%s\""
                        + "}",
                escapeJsonString(title),
                escapeJsonString(queueNo),
                LocalDateTime.now().toString()
        );
    }


    /**
     * JSON字符串转义
     */
    private String escapeJsonString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

}
