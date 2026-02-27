package com.tyut.service;

import com.tyut.entity.ExaminationReport;

public interface ExaminationReportService {
    void save(ExaminationReport examinationReport);
    ExaminationReport getById(Long id);
}
