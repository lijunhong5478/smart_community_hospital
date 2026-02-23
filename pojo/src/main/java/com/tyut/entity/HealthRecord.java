package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 健康档案，每个用户只有一个
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecord {
    private Long id;
    private Long residentId;
    private String title;
    private LocalDateTime updateTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
