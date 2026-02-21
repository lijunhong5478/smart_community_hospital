package com.tyut.vo;

import lombok.Data;

@Data
public class ResidentDetailVO {
    // ===== sys_user =====
    private Long userId;
    private String username;
    private String phone;
    private String avatarUrl;
    private Integer status;

    // ===== resident_profile =====
    private Long residentId;
    private String name;
    private Integer gender;
    private Integer age;
    private String contact;
    private String address;
}
