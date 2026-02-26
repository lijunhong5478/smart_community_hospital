package com.tyut.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentQueryDTO {
    private Long doctorId;
    private Long residentId;
    private Integer appointmentStatus;
    private Integer visitStatus;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime appointmentTime;
    private Integer pageNum;
    private Integer pageSize;
}
