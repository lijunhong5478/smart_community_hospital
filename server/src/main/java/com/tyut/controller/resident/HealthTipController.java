package com.tyut.controller.resident;

import com.tyut.dto.HealthTipQueryDTO;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.HealthTipService;
import com.tyut.vo.HealthTipVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("residentHealthTipController")
@RequestMapping("/resident/health-tips")
@Api(tags = "居民-健康宣教接口")
@Slf4j
public class HealthTipController {

    @Resource
    private HealthTipService healthTipService;

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询健康宣教详情")
    public Result<HealthTipVO> getHealthTipById(@PathVariable Long id) {
        HealthTipVO healthTipVO = healthTipService.getHealthTipById(id);
        return Result.success(healthTipVO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询健康宣教")
    public Result<PageResult> queryHealthTips(HealthTipQueryDTO queryDTO) {
        PageResult pageResult = healthTipService.queryHealthTips(queryDTO);
        return Result.success(pageResult);
    }
}
