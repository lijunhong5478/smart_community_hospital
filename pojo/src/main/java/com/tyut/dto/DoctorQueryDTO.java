package com.tyut.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQueryDTO {
    private Long departmentId;//部门
    private Integer weekDay;//时间
    private String timeSlot;
    private String name;//姓名
    private Integer title;//职称
    private Integer pageSize;
    private Integer pageNum;
}
