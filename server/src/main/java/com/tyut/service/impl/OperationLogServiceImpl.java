package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.dto.OperationLogQueryDTO;
import com.tyut.entity.OperationLog;
import com.tyut.mapper.OperationLogMapper;
import com.tyut.result.PageResult;
import com.tyut.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Override
    public PageResult list(OperationLogQueryDTO queryDTO) {
        LambdaQueryWrapper<OperationLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(OperationLog::getCreateTime, queryDTO.getEndDate())
                .ge(OperationLog::getCreateTime, queryDTO.getStartDate());
        Page<OperationLog> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<OperationLog> pageData = operationLogMapper.selectPage(page, queryWrapper);
        return new PageResult(pageData.getTotal(), pageData.getRecords());
    }
}
