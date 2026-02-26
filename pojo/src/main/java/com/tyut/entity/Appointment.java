package com.tyut.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Long id;
    private Long residentId;
    private Long doctorId;
    private Long scheduleId;
    private Integer triageLevel;
    private String symptom;
    private Integer appointmentStatus;
    private Integer visitStatus;
    private String cancelReason;
    private LocalDateTime cancelTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    private String queueNo;
}
