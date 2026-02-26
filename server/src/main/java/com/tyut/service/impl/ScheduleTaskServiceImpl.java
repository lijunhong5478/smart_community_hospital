package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tyut.constant.AppointConstant;
import com.tyut.constant.VisitConstant;
import com.tyut.entity.Appointment;
import com.tyut.mapper.AppointmentMapper;
import com.tyut.mapper.DoctorScheduleMapper;
import com.tyut.service.ScheduleTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Scheduled(cron = "0 0 0 ? * MON")
    @Override
    public void resetWeeklyScheduleNumbers() {
        log.info("开始执行每周排班人数重置任务 -{}", LocalDateTime.now());
        try{
            int updateCount=doctorScheduleMapper.resetCurrentNumber();
            log.info("重置成功，更新了{}行记录",updateCount);
        }catch (Exception e){
            log.error("执行每周排班人数重置任务异常",e);
        }
    }
    @Scheduled(cron = "0 0 0 ? * MON")
    @Override
    public void resetScheduleStatus() {
        log.info("执行每周排班状态重置任务 -{}",LocalDateTime.now());
        try{
            int updateCount=doctorScheduleMapper.resetScheduleStatus();
            log.info("重置成功，更新了{}行记录",updateCount);
        }catch (Exception e){
            log.error("执行每周排班状态重置任务异常",e);
        }
    }
    @Scheduled(cron="0 0 0 * * ?")
    @Override
    public void checkAppointment() {
        log.info("检查是否有逾期未就诊的预约");
        LambdaQueryWrapper<Appointment> wrapper=new LambdaQueryWrapper<>();
        wrapper.lt(Appointment::getAppointmentDate, LocalDate.now())
                .eq(Appointment::getAppointmentStatus, AppointConstant.BOOKED)
                .eq(Appointment::getVisitStatus, VisitConstant.WAITING);
        List<Appointment> appointments = appointmentMapper.selectList(wrapper);
        // 批量更新状态
        if (!appointments.isEmpty()) {
            List<Long> appointmentIds = appointments.stream()
                    .map(Appointment::getId)
                    .collect(Collectors.toList());

            LambdaUpdateWrapper<Appointment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Appointment::getId, appointmentIds)
                    .set(Appointment::getAppointmentStatus, AppointConstant.EXPIRED);

            appointmentMapper.update(null, updateWrapper);

            log.info("发现并处理逾期预约记录：{}条", appointments.size());
        } else {
            log.info("未发现逾期预约记录");
        }
    }
}
