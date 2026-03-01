package com.tyut.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisReportQueryDTO {
    private Long healthRecordId;//非空
    private LocalDate createDate;
    private Integer pageNum;
    private Integer pageSize;
}
