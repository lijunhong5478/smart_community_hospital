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
public class ConsultationSession {
    private Long id;
    private Long residentId;
    private Long doctorId;
    private Integer status;
    private LocalDateTime createTime;
}
