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
public class EpidemicAlert {
    private Long id;
    private String region;
    private Integer riskLevel;
    private String message;
    private Integer isDeleted;
    private LocalDateTime publishTime;
}
