package com.tyut.vo;

import com.tyut.entity.DiagnosisReport;
import com.tyut.entity.ExaminationReport;
import com.tyut.entity.PhysicalExamRecord;
import com.tyut.entity.ResidentMedicalHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecordVO {
    private Long id;
    private Long residentId;
    private String title;
    private LocalDateTime updateTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private List<DiagnosisReport> diagnosisReports;
    private List<ExaminationReport> examinationReports;
    private List<PhysicalExamRecord> physicalExamRecords;
    private List<ResidentMedicalHistory> residentMedicalHistories;
}
