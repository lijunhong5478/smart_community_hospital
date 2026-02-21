package com.tyut.dto;

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
    private Long departmentId;
    private Integer gender;
}
