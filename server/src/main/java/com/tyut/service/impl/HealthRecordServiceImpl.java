package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.dto.HealthRecordQueryDTO;
import com.tyut.entity.*;
import com.tyut.mapper.*;
import com.tyut.result.PageResult;
import com.tyut.service.HealthRecordService;
import com.tyut.vo.HealthRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {
    
    @Autowired
    private HealthRecordMapper healthRecordMapper;
    
    @Autowired
    private ResidentMapper residentMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private DiagnosisReportMapper diagnosisReportMapper;
    
    @Autowired
    private ExaminationReportMapper examinationReportMapper;
    
    @Autowired
    private PhysicalExamRecordMapper physicalExamRecordMapper;
    
    @Autowired
    private ResidentMedicalHistoryMapper residentMedicalHistoryMapper;
    
    @Override
    public PageResult list(HealthRecordQueryDTO queryDTO) {
        // 构建分页对象
        Page<HealthRecord> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<HealthRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthRecord::getIsDeleted, 0); // 只查询未删除的记录
        
        // 根据不同条件进行查询
        if (queryDTO.getUserId() != null) {
            queryWrapper.eq(HealthRecord::getResidentId, queryDTO.getUserId());
        }
        
        // 如果提供了姓名、身份证或电话号码，需要联表查询
        if (StringUtils.hasText(queryDTO.getRealName()) || 
            StringUtils.hasText(queryDTO.getIdCard()) || 
            StringUtils.hasText(queryDTO.getPhone())) {
            
            // 先查询符合条件的用户ID
            List<Long> userIds = getUserIdsByConditions(queryDTO);
            if (userIds.isEmpty()) {
                // 如果没有找到符合条件的用户，返回空结果
                return new PageResult(0L, List.of());
            }
            queryWrapper.in(HealthRecord::getResidentId, userIds);
        }
        
        // 执行分页查询
        IPage<HealthRecord> pageData = healthRecordMapper.selectPage(page, queryWrapper);
        
        // 转换为VO对象
        List<HealthRecordVO> voList = pageData.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult(pageData.getTotal(), voList);
    }
    
    /**
     * 根据查询条件获取用户ID列表
     */
    private List<Long> getUserIdsByConditions(HealthRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getIsDeleted, 0);
        
        // 根据不同条件查询用户
        if (StringUtils.hasText(queryDTO.getRealName())) {
            // 通过居民真实姓名查询
            LambdaQueryWrapper<ResidentProfile> residentWrapper = new LambdaQueryWrapper<>();
            residentWrapper.like(ResidentProfile::getName, queryDTO.getRealName());
            List<ResidentProfile> residents = residentMapper.selectList(residentWrapper);
            List<Long> residentUserIds = residents.stream()
                    .map(ResidentProfile::getUserId)
                    .collect(Collectors.toList());
            if (!residentUserIds.isEmpty()) {
                userWrapper.in(SysUser::getId, residentUserIds);
            }
        }
        
        if (StringUtils.hasText(queryDTO.getIdCard())) {
            userWrapper.eq(SysUser::getIdCard, queryDTO.getIdCard());
        }
        
        if (StringUtils.hasText(queryDTO.getPhone())) {
            userWrapper.eq(SysUser::getPhone, queryDTO.getPhone());
        }
        
        List<SysUser> users = userMapper.selectList(userWrapper);
        return users.stream()
                .map(SysUser::getId)
                .collect(Collectors.toList());
    }
    
    /**
     * 将HealthRecord实体转换为HealthRecordVO
     */
    private HealthRecordVO convertToVO(HealthRecord healthRecord) {
        HealthRecordVO vo = new HealthRecordVO();
        BeanUtils.copyProperties(healthRecord, vo);
        
        Long recordId = healthRecord.getId();
        
        // 查询相关的诊断报告
        LambdaQueryWrapper<DiagnosisReport> diagnosisWrapper = new LambdaQueryWrapper<>();
        diagnosisWrapper.eq(DiagnosisReport::getHealthRecordId, recordId)
                .eq(DiagnosisReport::getIsDeleted, 0);
        List<DiagnosisReport> diagnosisReports = diagnosisReportMapper.selectList(diagnosisWrapper);
        vo.setDiagnosisReports(diagnosisReports);
        
        // 查询相关的检查报告
        LambdaQueryWrapper<ExaminationReport> examWrapper = new LambdaQueryWrapper<>();
        examWrapper.eq(ExaminationReport::getRecordId, recordId)
                .eq(ExaminationReport::getIsDeleted, 0);
        List<ExaminationReport> examinationReports = examinationReportMapper.selectList(examWrapper);
        vo.setExaminationReports(examinationReports);
        
        // 查询相关的体检记录
        LambdaQueryWrapper<PhysicalExamRecord> physicalWrapper = new LambdaQueryWrapper<>();
        physicalWrapper.eq(PhysicalExamRecord::getRecordId, recordId)
                .eq(PhysicalExamRecord::getIsDeleted, 0);
        List<PhysicalExamRecord> physicalExamRecords = physicalExamRecordMapper.selectList(physicalWrapper);
        vo.setPhysicalExamRecords(physicalExamRecords);
        
        // 查询相关的病史记录
        LambdaQueryWrapper<ResidentMedicalHistory> historyWrapper = new LambdaQueryWrapper<>();
        historyWrapper.eq(ResidentMedicalHistory::getRecordId, recordId)
                .eq(ResidentMedicalHistory::getIsDeleted, 0);
        List<ResidentMedicalHistory> medicalHistories = residentMedicalHistoryMapper.selectList(historyWrapper);
        vo.setResidentMedicalHistories(medicalHistories);
        
        return vo;
    }
}