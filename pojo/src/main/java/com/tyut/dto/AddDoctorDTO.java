package com.tyut.dto;

import com.tyut.entity.DoctorSchedule;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "添加医生DTO")
public class AddDoctorDTO {
    // ===== sys_user =====
    private String username;
    private String phone;
    private String idCard;
    private String password;
    private String avatarUrl;

    // ===== doctor_profile =====
    private String name;
    private String specialty;
    private Integer title;
    private String introduction;
    private Integer departmentId;
    private Integer gender;
    private Integer age;

    // ===== doctor_schedule =====
    List<DoctorSchedule> doctorSchedules;

}
