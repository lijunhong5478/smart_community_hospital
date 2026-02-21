package com.tyut.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private Integer loginType;//指定登录类型

    private String account;  // 可能是手机号/身份证/用户名

    private String password;
}
