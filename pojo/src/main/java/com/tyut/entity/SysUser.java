package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {
    private Long id;

    private String username;

    private String phone;

    private String idCard;

    private String password;

    private String avatarUrl;

    private Integer status;      // 1正常 0禁用

    private Integer isDeleted;   // 0正常 1已删除

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer roleType;    // 0管理员 1医生 2居民
}
