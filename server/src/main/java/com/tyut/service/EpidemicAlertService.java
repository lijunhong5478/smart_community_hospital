package com.tyut.service;

import com.tyut.dto.EpidemicAlertQueryDTO;
import com.tyut.entity.EpidemicAlert;
import com.tyut.result.PageResult;
import com.tyut.vo.EpidemicAlertVO;

public interface EpidemicAlertService {

    /**
     * 发布疫情预警
     *
     * @param epidemicAlert 疫情预警信息
     */
    void publishEpidemicAlert(EpidemicAlert epidemicAlert);

    /**
     * 根据ID查询疫情预警详情
     *
     * @param id 预警ID
     * @return 疫情预警VO
     */
    EpidemicAlertVO getEpidemicAlertById(Long id);

    /**
     * 条件分页查询疫情预警
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult queryEpidemicAlerts(EpidemicAlertQueryDTO queryDTO);

    /**
     * 删除疫情预警（逻辑删除）
     *
     * @param id 预警ID
     */
    void deleteEpidemicAlert(Long id);

    /**
     * 获取最近一次疫情预警
     *
     * @return 疫情预警VO
     */
    EpidemicAlertVO getLatestEpidemicAlert();

}
