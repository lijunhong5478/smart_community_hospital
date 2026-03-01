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
public class HealthTips {
    private Long id;
    private Integer type;
    private String title;
    private String content;
    private Long publisherId;
    private Integer isDeleted;
    private LocalDateTime publishTime;
}
