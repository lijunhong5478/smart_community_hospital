package com.tyut.service;

import com.tyut.dto.HealthRecordQueryDTO;
import com.tyut.entity.MedicalVisit;
import com.tyut.result.PageResult;
import com.tyut.vo.HealthRecordVO;

public interface HealthRecordService {
    
    /**
     * 根据条件查询健康档案信息
     * @param queryDTO 查询条件DTO
     * @return 分页结果，包含HealthRecordVO列表
     */
    PageResult list(HealthRecordQueryDTO queryDTO);
}
