package com.tyut.vo;

import com.tyut.entity.DoctorSchedule;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDetailVO {
    // ===== sys_user =====
    private Long userId;
    private String username;
    private String phone;
    private String avatarUrl;
    private Integer status;

    // ===== doctor_profile =====
    private Long doctorId;
    private String name;
    private String specialty;
    private Integer title;
    private String introduction;
    private Integer gender;
    private Integer age;

    // ===== department =====
    private Long departmentId;
    private String departmentName;

    // ===== doctor_schedule =====
    private List<DoctorSchedule> doctorSchedules;
}
