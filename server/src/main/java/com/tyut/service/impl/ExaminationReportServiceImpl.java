package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.annotation.DataBackUp;
import com.tyut.constant.AccountConstant;
import com.tyut.constant.ModuleConstant;
import com.tyut.dto.ExaminationReportQueryDTO;
import com.tyut.entity.ExaminationReport;
import com.tyut.mapper.ExaminationReportMapper;
import com.tyut.result.PageResult;
import com.tyut.service.ExaminationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExaminationReportServiceImpl implements ExaminationReportService {
    @Autowired
    private ExaminationReportMapper examinationReportMapper;
    @DataBackUp(module = ModuleConstant.HEALTH_RECORD)
    @Override
    public void saveBatch(List<ExaminationReport> examinationReports) {
        examinationReports.forEach(examinationReport -> {
            examinationReport.setIsDeleted(AccountConstant.NOT_DELETE);
            examinationReport.setCreateTime(LocalDateTime.now());
            examinationReportMapper.insert(examinationReport);
        });
    }

    @Override
    public ExaminationReport getById(Long id) {
        return examinationReportMapper.selectById(id);
    }

    @Override
    public PageResult list(ExaminationReportQueryDTO examinationReportQueryDTO) {
        Page page=new Page(examinationReportQueryDTO.getPageNum(),examinationReportQueryDTO.getPageSize());
        LambdaQueryWrapper<ExaminationReport> queryWrapper = new LambdaQueryWrapper<>();
        if(examinationReportQueryDTO.getHealthRecordId()!=null){
            queryWrapper.eq(ExaminationReport::getRecordId,examinationReportQueryDTO.getHealthRecordId());
        }
        if(examinationReportQueryDTO.getReportType()!=null){
            queryWrapper.eq(ExaminationReport::getReportType,examinationReportQueryDTO.getReportType());
        }
        if(examinationReportQueryDTO.getCreateDate()!=null){
            queryWrapper.between(ExaminationReport::getCreateTime, examinationReportQueryDTO.getCreateDate().atStartOfDay()
                    ,examinationReportQueryDTO.getCreateDate().plusDays(1).atStartOfDay());
        }
        IPage<ExaminationReport> pageData = examinationReportMapper.selectPage(page, queryWrapper);
        return PageResult.builder()
                .total(pageData.getTotal())
                .dataList(pageData.getRecords())
                .build();
    }
}
