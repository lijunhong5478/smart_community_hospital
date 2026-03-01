package com.tyut.service;

import com.tyut.entity.PhysicalExamRecord;
import com.tyut.result.PageResult;

public interface PhysicalExamRecordService {
    /**
     * 医生新增体检记录
     * @param physicalExamRecord 体检记录实体
     */
    void saveByDoctor(PhysicalExamRecord physicalExamRecord);

    /**
     * 根据ID查询体检记录
     * @param id 体检记录ID
     * @return 体检记录实体
     */
    PhysicalExamRecord getById(Long id);

    /**
     * 居民查询自己的体检记录列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param recordId 健康档案ID
     * @return 分页结果
     */
    PageResult listByResident(Integer pageNum, Integer pageSize, Long recordId);

    /**
     * 查询居民最近一次的体检记录
     * @param recordId 健康档案ID
     * @return 最近一次体检记录
     */
    PhysicalExamRecord getLatestByResident(Long recordId);

    /**
     * 医生查询体检记录列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param recordId 健康档案ID
     * @return 分页结果
     */
    PageResult listByDoctor(Integer pageNum, Integer pageSize, Long recordId);
}
