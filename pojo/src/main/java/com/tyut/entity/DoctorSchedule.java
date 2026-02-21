package com.tyut.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DoctorSchedule {
    private Long id;

    private Long doctorId;

    private LocalDate scheduleDate;

    private String timeSlot;      // AM/PM

    private Integer maxNumber;

    private Integer currentNumber;

    private Integer status;       // 1正常 0停诊

    private Integer isDeleted;

    private LocalDateTime createTime;
}
