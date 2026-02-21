package com.tyut.dto;

import lombok.Data;


@Data
public class ResidentRegisterDTO {

    // ===== sys_user =====
    private String username;

    private String phone;

    private String password;

    private String idCard;

    private String avatarUrl;

    // ===== resident_profile =====

    private String name;

    private Integer gender;   // 0女 1男

    private Integer age;

    private String contact;

    private String address;
}