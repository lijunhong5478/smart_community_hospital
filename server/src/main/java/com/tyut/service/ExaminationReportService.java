package com.tyut.service;

import com.tyut.dto.ExaminationReportQueryDTO;
import com.tyut.entity.ExaminationReport;
import com.tyut.result.PageResult;

import java.util.List;

public interface ExaminationReportService {
    void saveBatch(List<ExaminationReport> examinationReports);
    ExaminationReport getById(Long id);
    PageResult list(ExaminationReportQueryDTO examinationReportQueryDTO);
}
