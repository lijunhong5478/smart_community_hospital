package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 诊断结果报告
 * 每次诊断必生成
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisReport {
    private Long id;
    private Long visitId;
    private String diagnosisResult;
    private String diagnosisDetail;
    private LocalDateTime createTime;
    private Integer isDeleted;
    private Long healthRecordId;
}
