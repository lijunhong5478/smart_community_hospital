package com.tyut.service.impl;

import com.tyut.mapper.DoctorScheduleMapper;
import com.tyut.service.ScheduleTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;
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
}
