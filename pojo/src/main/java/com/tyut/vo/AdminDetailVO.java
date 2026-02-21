package com.tyut.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailVO {
    private Long id;
    private String username;
    private String phone;
    private String avatarUrl;
}
