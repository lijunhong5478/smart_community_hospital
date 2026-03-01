package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.annotation.DataBackUp;
import com.tyut.constant.ModuleConstant;
import com.tyut.dto.HealthTipQueryDTO;
import com.tyut.entity.HealthTips;
import com.tyut.exception.BaseException;
import com.tyut.mapper.HealthTipMapper;
import com.tyut.mapper.UserMapper;
import com.tyut.result.PageResult;
import com.tyut.service.HealthTipService;
import com.tyut.vo.HealthTipVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthTipServiceImpl implements HealthTipService {

    @Resource
    private HealthTipMapper healthTipMapper;

    @Resource
    private UserMapper userMapper;
    @DataBackUp(module = ModuleConstant.HEALTH_EDUCATION_PUBLISH)
    @Override
    public void publishHealthTip(HealthTips healthTips) {
        // 设置发布者信息
        healthTips.setPublisherId(healthTips.getPublisherId());
        healthTips.setPublishTime(LocalDateTime.now());
        healthTips.setIsDeleted(0);

        // 保存到数据库
        healthTipMapper.insert(healthTips);
    }

    @Override
    public HealthTipVO getHealthTipById(Long id) {
        HealthTips healthTips = healthTipMapper.selectById(id);
        if (healthTips == null || healthTips.getIsDeleted().equals(1)) {
            throw new BaseException("宣教不存在或已被删除");
        }

        HealthTipVO healthTipVO = new HealthTipVO();
        BeanUtils.copyProperties(healthTips, healthTipVO);

        // 获取发布者姓名
        String publisherName = userMapper.selectById(healthTips.getPublisherId()).getUsername();
        healthTipVO.setPublisherName(publisherName);

        return healthTipVO;
    }

    @Override
    public PageResult queryHealthTips(HealthTipQueryDTO queryDTO) {
        // 构建分页对象
        Page<HealthTips> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<HealthTips> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HealthTips::getIsDeleted, 0);

        // 添加类型筛选条件
        if (queryDTO.getType() != null) {
            queryWrapper.eq(HealthTips::getType, queryDTO.getType());
        }

        // 添加标题模糊查询条件
        if (StringUtils.hasText(queryDTO.getTitle())) {
            queryWrapper.like(HealthTips::getTitle, queryDTO.getTitle());
        }

        // 添加时间范围查询条件
        if (queryDTO.getStartTime() != null) {
            queryWrapper.ge(HealthTips::getPublishTime, queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            queryWrapper.le(HealthTips::getPublishTime, queryDTO.getEndTime());
        }

        // 按发布时间降序排列
        queryWrapper.orderByDesc(HealthTips::getPublishTime);

        // 执行分页查询
        Page<HealthTips> resultPage = healthTipMapper.selectPage(page, queryWrapper);

        // 转换为VO对象
        List<HealthTipVO> records = resultPage.getRecords().stream().map(healthTips -> {
            HealthTipVO healthTipVO = new HealthTipVO();
            BeanUtils.copyProperties(healthTips, healthTipVO);

            // 获取发布者姓名
            String publisherName = userMapper.selectById(healthTips.getId()).getUsername();
            healthTipVO.setPublisherName(publisherName);

            return healthTipVO;
        }).collect(Collectors.toList());

        return new PageResult(resultPage.getTotal(), records);
    }

    @Override
    public void deleteHealthTip(Long id) {
        HealthTips healthTips = healthTipMapper.selectById(id);
        if (healthTips == null || healthTips.getIsDeleted().equals(1)) {
            throw new BaseException("宣教不存在或已被删除");
        }

        // 逻辑删除
        healthTips.setIsDeleted(1);
        healthTipMapper.updateById(healthTips);
    }
}
