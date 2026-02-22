package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {
    private Long id;
    private Long userId;
    private String name;
    private String specialty;
    private Integer title;
    private String introduction;
    private LocalDateTime createTime;
    private Integer departmentId;
    private Integer gender;
    private Integer age;
}
