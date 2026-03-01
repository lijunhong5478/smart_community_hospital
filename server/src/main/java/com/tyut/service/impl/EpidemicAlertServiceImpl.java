package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.annotation.DataBackUp;
import com.tyut.constant.ModuleConstant;
import com.tyut.context.UserContext;
import com.tyut.dto.EpidemicAlertQueryDTO;
import com.tyut.entity.EpidemicAlert;
import com.tyut.exception.BaseException;
import com.tyut.mapper.EpidemicAlertMapper;
import com.tyut.result.PageResult;
import com.tyut.service.EpidemicAlertService;
import com.tyut.vo.EpidemicAlertVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpidemicAlertServiceImpl implements EpidemicAlertService {

    @Resource
    private EpidemicAlertMapper epidemicAlertMapper;
    @DataBackUp(module = ModuleConstant.EPIDEMIC_ALERT_PUBLISH)
    @Override
    public void publishEpidemicAlert(EpidemicAlert epidemicAlert) {

        // 设置发布时间和删除状态
        epidemicAlert.setPublishTime(LocalDateTime.now());
        epidemicAlert.setIsDeleted(0);

        // 保存到数据库
        epidemicAlertMapper.insert(epidemicAlert);
    }

    @Override
    public EpidemicAlertVO getEpidemicAlertById(Long id) {
        EpidemicAlert epidemicAlert = epidemicAlertMapper.selectById(id);
        if (epidemicAlert == null || epidemicAlert.getIsDeleted().equals(1)) {
            throw new BaseException("疫情预警不存在或已被删除");
        }

        EpidemicAlertVO epidemicAlertVO = new EpidemicAlertVO();
        BeanUtils.copyProperties(epidemicAlert, epidemicAlertVO);

        return epidemicAlertVO;
    }

    @Override
    public PageResult queryEpidemicAlerts(EpidemicAlertQueryDTO queryDTO) {
        // 构建分页对象
        Page<EpidemicAlert> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<EpidemicAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EpidemicAlert::getIsDeleted, 0);

        // 添加地区模糊查询条件
        if (StringUtils.hasText(queryDTO.getRegion())) {
            queryWrapper.like(EpidemicAlert::getRegion, queryDTO.getRegion());
        }

        // 添加风险等级筛选条件
        if (queryDTO.getRiskLevel() != null) {
            queryWrapper.eq(EpidemicAlert::getRiskLevel, queryDTO.getRiskLevel());
        }

        // 添加时间范围查询条件
        if (queryDTO.getStartTime() != null) {
            queryWrapper.ge(EpidemicAlert::getPublishTime, queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            queryWrapper.le(EpidemicAlert::getPublishTime, queryDTO.getEndTime());
        }

        // 按发布时间降序排列
        queryWrapper.orderByDesc(EpidemicAlert::getPublishTime);

        // 执行分页查询
        Page<EpidemicAlert> resultPage = epidemicAlertMapper.selectPage(page, queryWrapper);

        // 转换为VO对象
        List<EpidemicAlertVO> records = resultPage.getRecords().stream().map(epidemicAlert -> {
            EpidemicAlertVO epidemicAlertVO = new EpidemicAlertVO();
            BeanUtils.copyProperties(epidemicAlert, epidemicAlertVO);
            return epidemicAlertVO;
        }).collect(Collectors.toList());

        return new PageResult(resultPage.getTotal(), records);
    }

    @Override
    public void deleteEpidemicAlert(Long id) {
        EpidemicAlert epidemicAlert = epidemicAlertMapper.selectById(id);
        if (epidemicAlert == null || epidemicAlert.getIsDeleted().equals(1)) {
            throw new BaseException("疫情预警不存在或已被删除");
        }

        // 逻辑删除
        epidemicAlert.setIsDeleted(1);
        epidemicAlertMapper.updateById(epidemicAlert);
    }

    @Override
    public EpidemicAlertVO getLatestEpidemicAlert() {
        // 构建查询条件
        LambdaQueryWrapper<EpidemicAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EpidemicAlert::getIsDeleted, 0);
        // 按发布时间降序排列，取第一条
        queryWrapper.orderByDesc(EpidemicAlert::getPublishTime);

        // 执行查询
        List<EpidemicAlert> result = epidemicAlertMapper.selectList(queryWrapper);

        if (result.isEmpty()) {
            throw new BaseException("暂无疫情预警信息");
        }

        EpidemicAlert latestAlert = result.get(0);
        EpidemicAlertVO epidemicAlertVO = new EpidemicAlertVO();
        BeanUtils.copyProperties(latestAlert, epidemicAlertVO);

        return epidemicAlertVO;
    }

}
