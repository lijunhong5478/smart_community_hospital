package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.constant.AccountConstant;
import com.tyut.constant.AppointConstant;
import com.tyut.constant.DiseaseLevelConstant;
import com.tyut.constant.VisitConstant;
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
import com.tyut.websocket.WebSocketServer;
import dev.langchain4j.model.ollama.OllamaChatModel;
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
    private OllamaChatModel chatModel;
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
        Integer triageLevel = determineTriageLevel(appointment.getSymptom());
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
    }

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
    }

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
     * 根据症状描述确定分诊等级
     *
     * @param symptom
     * @return
     */
    private Integer determineTriageLevel(String symptom) {
        if (symptom == null || symptom.trim().isEmpty()) {
            return DiseaseLevelConstant.LEVEL_4_MINOR;
        }

        try {
            // 1. 使用角色设定与明确的任务边界
            // 2. 引入 Few-Shot (少样本) 引导模型模仿输出
            // 3. 增加强力后缀约束
            String prompt = "### Role\n" +
                    "你是一位急诊科分诊专家。\n\n" +
                    "### Task\n" +
                    "根据患者症状描述，从[1, 2, 3, 4]中选择一个分级数字。只需输出数字，严禁输出任何文字解释。\n\n" +
                    "### Rules\n" +
                    "1级: 危急(胸痛/呼吸停止/大出血/意识丧失)\n" +
                    "2级: 急重(剧痛/高热39℃+/严重外伤)\n" +
                    "3级: 普通(轻度腹痛/低烧/咳嗽)\n" +
                    "4级: 非紧急(普通感冒/轻微擦伤/复诊)\n\n" +
                    "### Examples\n" +
                    "症状：我突然觉得喘不上气，胸口像被大石头压着。\n" +
                    "结果：1\n" +
                    "症状：孩子感冒流鼻涕两天了。\n" +
                    "结果：4\n\n" +
                    "### Input\n" +
                    "症状：" + symptom + "\n" +
                    "结果：";

            String response = chatModel.generate(prompt).trim();

            // 鲁棒性处理：只取返回内容的第一个字符（防止模型在数字后面加句号或废话）
            if (response.length() > 0) {
                // 提取字符串中的第一个数字
                String firstDigit = response.replaceAll("[^1-4]", "");
                if (!firstDigit.isEmpty()) {
                    int level = Character.getNumericValue(firstDigit.charAt(0));
                    return level;
                }
            }

            return getLevelByKeyword(symptom);
        } catch (Exception e) {
            return getLevelByKeyword(symptom);
        }
    }

    /**
     * 关键词匹配备用方案（优化版）
     * 逻辑：从致命性症状向轻微症状逐级过滤
     */
    private int getLevelByKeyword(String symptom) {
        if (symptom == null) return DiseaseLevelConstant.LEVEL_4_MINOR;
        String s = symptom.toLowerCase();

        // --- 1级：危急 (Life-threatening) ---
        // 侧重于意识、呼吸、循环三大体征
        if (containsAny(s, "胸痛", "心梗", "呼吸困难", "窒息", "昏迷", "意识丧失",
                "大出血", "休克", "心跳骤停", "抽搐", "中毒")) {
            return DiseaseLevelConstant.LEVEL_1_CRITICAL;
        }

        // --- 2级：急重 (Emergency) ---
        // 侧重于“剧烈”程度和高风险器官症状
        if (containsAny(s, "剧烈", "剧痛", "高热", "39度", "40度", "严重外伤",
                "骨折", "吞咽困难", "呼吸急促", "急性过敏")) {
            return DiseaseLevelConstant.LEVEL_2_SEVERE;
        }

        // --- 3级：普通急诊 (Urgent) ---
        // 侧重于需要尽快处理的急性症状，但无生命危险
        if (containsAny(s, "腹痛", "呕吐", "腹泻", "中度", "扭伤", "头晕",
                "异物", "割伤", "尿频尿急", "持续发烧")) {
            return DiseaseLevelConstant.LEVEL_3_MODERATE;
        }

        // --- 4级：非紧急 (Non-urgent) ---
        // 侧重于慢性病、轻微感冒、复诊、开药
        // 默认或匹配以下关键词：
        if (containsAny(s, "感冒", "流鼻涕", "咳嗽", "打喷嚏", "擦伤",
                "复诊", "咨询", "体检", "配药", "轻微")) {
            return DiseaseLevelConstant.LEVEL_4_MINOR;
        }

        return DiseaseLevelConstant.LEVEL_4_MINOR;
    }

    /**
     * 辅助方法：判断字符串是否包含数组中的任意关键词
     */
    private boolean containsAny(String target, String... keywords) {
        for (String key : keywords) {
            if (target.contains(key)) return true;
        }
        return false;
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
