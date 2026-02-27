package com.tyut.service.impl;

import com.tyut.constant.AccountConstant;
import com.tyut.entity.ExaminationReport;
import com.tyut.mapper.ExaminationReportMapper;
import com.tyut.service.ExaminationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExaminationReportServiceImpl implements ExaminationReportService {
    @Autowired
    private ExaminationReportMapper examinationReportMapper;
    @Override
    public void save(ExaminationReport examinationReport) {
        examinationReport.setIsDeleted(AccountConstant.NOT_DELETE);
        examinationReport.setCreateTime(LocalDateTime.now());
        examinationReportMapper.insert(examinationReport);
    }

    @Override
    public ExaminationReport getById(Long id) {
        return examinationReportMapper.selectById(id);
    }
}
