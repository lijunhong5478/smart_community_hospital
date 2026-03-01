package com.tyut.controller.doctor;

import com.tyut.dto.EpidemicAlertQueryDTO;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.EpidemicAlertService;
import com.tyut.vo.EpidemicAlertVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("doctorEpidemicAlertController")
@RequestMapping("/doctor/epidemic-alerts")
@Api(tags = "医生-疫情预警查看")
@Slf4j
public class EpidemicAlertController {

    @Resource
    private EpidemicAlertService epidemicAlertService;

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询疫情预警详情")
    public Result<EpidemicAlertVO> getEpidemicAlertById(@PathVariable Long id) {
        EpidemicAlertVO epidemicAlertVO = epidemicAlertService.getEpidemicAlertById(id);
        return Result.success(epidemicAlertVO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询疫情预警")
    public Result<PageResult> queryEpidemicAlerts(EpidemicAlertQueryDTO queryDTO) {
        PageResult pageResult = epidemicAlertService.queryEpidemicAlerts(queryDTO);
        return Result.success(pageResult);
    }
}
