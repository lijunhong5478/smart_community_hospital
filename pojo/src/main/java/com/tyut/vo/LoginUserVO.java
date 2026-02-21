package com.tyut.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO {
    private Long id;

    private String username;

    private String avatarUrl;

    private Integer roleType;

    private String token;
}
