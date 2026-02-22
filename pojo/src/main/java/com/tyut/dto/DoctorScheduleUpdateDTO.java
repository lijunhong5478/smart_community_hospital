package com.tyut.dto;

import com.tyut.entity.DoctorSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleUpdateDTO {
    private Long doctorId;
    private List<DoctorSchedule> doctorSchedules;
}
