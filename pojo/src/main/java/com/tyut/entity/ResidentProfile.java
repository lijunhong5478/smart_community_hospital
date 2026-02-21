package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentProfile {
    private Long id;
    private Long userId;
    private String name;
    private Integer gender;
    private Integer age;
    private String contact;
    private String address;
    private Integer is_deleted;
    private LocalDateTime createTime;
}
