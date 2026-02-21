package com.tyut.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class UpdateProfileDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 姓名（仅居民可修改）
     */
    private String name;

    /**
     * 性别（仅居民可修改）
     */
    private Integer gender;

    /**
     * 年龄（仅居民可修改）
     */
    private Integer age;

    /**
     * 紧急联系方式（仅居民可修改）
     */
    private String contact;

    /**
     * 地址（仅居民可修改）
     */
    private String address;

    public boolean judgeResident(){
        if(Objects.isNull(name)&& Objects.isNull(gender)&& Objects.isNull(age)&& Objects.isNull(contact)&& Objects.isNull(address)){
            return false;
        }
        return true;
    }

    public boolean judgeUser(){
        if(Objects.isNull(username)&& Objects.isNull(phone)&& Objects.isNull(avatarUrl)){
            return false;
        }
        return true;
    }
}
