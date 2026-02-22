package com.tyut.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 社区医院营业时间为
 * 上午：8:00-12:00
 * 下午：14:00-18:00
 */

@Data
public class DoctorSchedule {
    private Long id;

    private Long doctorId;

    private Integer weekDay;

    private String timeSlot;      // AM/PM

    private Integer maxNumber;

    private Integer currentNumber;

    private Integer status;       // 1正常 0停诊

    private LocalDateTime createTime;
}
