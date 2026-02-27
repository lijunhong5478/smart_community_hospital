package com.tyut.controller.doctor;

import com.tyut.dto.HealthRecordQueryDTO;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.HealthRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("doctorHealthRecordController")
@RequestMapping("/doctor/healthRecord")
@Api(tags = "医生健康档案相关接口")
public class HealthRecordController {
    @Autowired
    private HealthRecordService healthRecordService;
    @GetMapping("/list")
    @ApiOperation("查询健康档案列表接口")
    public Result<PageResult> list(HealthRecordQueryDTO healthRecordQueryDTO) {
        return Result.success(healthRecordService.list(healthRecordQueryDTO));
    }
}
