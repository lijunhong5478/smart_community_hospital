package com.tyut.utils;



import com.tyut.entity.Appointment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class QueueManagerUtil {

    /**
     * 患者候诊对象，封装排序所需的最简信息
     */
    public static class WaitingPatient {
        private Appointment appointment;

        public WaitingPatient(Appointment appointment) {
            this.appointment = appointment;
        }

        // 获取计算后的优先级分值
        public long getPriorityScore() {
            // 将预约日期和时间组合成LocalDateTime
            LocalDateTime appointmentDateTime = LocalDateTime.of(
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTime()
            );
            // 转换为时间戳
            long timestamp = appointmentDateTime.toEpochSecond(ZoneOffset.UTC);
            
            // 基础分：时间戳
            long score = timestamp;

            // 权重补偿：病情每严重一级，分值减少 30 分钟 (1800秒)
            Integer triageLevel = appointment.getTriageLevel();
            if (triageLevel != null) {
                score -= (5 - triageLevel) * 1800L;
            }

            // 状态补偿：如果是"过号重新报到"(visitStatus=4)，分值增加 20 分钟惩罚，使其后移
            Integer visitStatus = appointment.getVisitStatus();
            if (visitStatus != null && visitStatus == 4) {
                score += 1200L;
            }

            return score;
        }

        public String getQueueNo() {
            return appointment.getQueueNo();
        }
        
        public Appointment getAppointment() {
            return appointment;
        }
    }

    /**
     * 获取当前排队顺序（模拟堆排序）
     * @param patients WaitingPatient集合
     * @return 有序的Appointment集合
     */
    public static List<Appointment> getCallOrder(List<WaitingPatient> patients) {
        // 使用小顶堆，根据 PriorityScore 排序
        PriorityQueue<WaitingPatient> heap = new PriorityQueue<>(
                Comparator.comparingLong(WaitingPatient::getPriorityScore)
        );

        heap.addAll(patients);

        // 依次弹出即为叫号顺序，返回Appointment列表
        return heap.stream()
                .sorted(Comparator.comparingLong(WaitingPatient::getPriorityScore))
                .map(WaitingPatient::getAppointment)
                .collect(Collectors.toList());
    }
}
