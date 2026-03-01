package com.tyut.service;

import com.tyut.dto.HealthTipQueryDTO;
import com.tyut.entity.HealthTips;
import com.tyut.result.PageResult;
import com.tyut.vo.HealthTipVO;

public interface HealthTipService {

    /**
     * 发布健康宣教
     * @param healthTips 健康宣教信息
     */
    void publishHealthTip(HealthTips healthTips);

    /**
     * 根据ID查询健康宣教详情
     * @param id 宣教ID
     * @return 健康宣教VO
     */
    HealthTipVO getHealthTipById(Long id);

    /**
     * 条件分页查询健康宣教
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult queryHealthTips(HealthTipQueryDTO queryDTO);

    /**
     * 删除健康宣教（逻辑删除）
     * @param id 宣教ID
     */
    void deleteHealthTip(Long id);
}
