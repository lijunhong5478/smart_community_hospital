package com.tyut.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationReportQueryDTO {
    private Long healthRecordId;
    private Integer reportType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    private Integer pageNum;
    private Integer pageSize;
}
