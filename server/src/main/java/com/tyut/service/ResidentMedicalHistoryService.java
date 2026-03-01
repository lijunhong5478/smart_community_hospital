package com.tyut.service;

import com.tyut.entity.ResidentMedicalHistory;
import com.tyut.result.PageResult;

public interface ResidentMedicalHistoryService {
    /**
     * 医生新增居民病史记录
     * @param medicalHistory 病史记录实体
     */
    void saveByDoctor(ResidentMedicalHistory medicalHistory);

    /**
     * 根据ID查询病史记录
     * @param id 病史记录ID
     * @return 病史记录实体
     */
    ResidentMedicalHistory getById(Long id);

    /**
     * 居民查询自己的病史记录
     * @param recordId 健康档案ID
     * @return 病史记录
     */
    ResidentMedicalHistory getByResident(Long recordId);

    /**
     * 医生查询病史记录列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param recordId 健康档案ID
     * @return 分页结果
     */
    PageResult listByDoctor(Integer pageNum, Integer pageSize, Long recordId);
}
