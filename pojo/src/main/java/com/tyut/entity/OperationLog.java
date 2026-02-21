package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationLog {
    private Long id;
    private Long userId;
    private Integer roleType;
    private String methodName;
    private String moduleName;
    private LocalDateTime createTime;
}
