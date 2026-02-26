package com.tyut.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "精确时间预约DTO")
public class ExactTimeAppointmentDTO {

    @ApiModelProperty(value = "居民ID", required = true)
    private Long residentId;

    @ApiModelProperty(value = "医生ID", required = true)
    private Long doctorId;

    @ApiModelProperty(value="计划id")
    private Long scheduleId;

    @ApiModelProperty(value = "预约日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @ApiModelProperty(value = "精确预约时间", required = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    @ApiModelProperty(value = "症状描述")
    private String symptom;
}
