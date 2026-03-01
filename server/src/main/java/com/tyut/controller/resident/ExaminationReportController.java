package com.tyut.controller.resident;

import com.tyut.dto.ExaminationReportQueryDTO;
import com.tyut.entity.ExaminationReport;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.ExaminationReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("residentExaminationReportController")
@RequestMapping("/resident/examinationReport")
@Api(tags="居民-检查报告接口")
public class ExaminationReportController {
    @Autowired
    private ExaminationReportService examinationReportService;
    @GetMapping("/{id}")
    @ApiOperation("根据id查询检查报告")
    public Result<ExaminationReport> getById(@PathVariable Long id){
        return Result.success(examinationReportService.getById(id));
    }
    @GetMapping("/list")
    @ApiOperation("查询检查报告列表")
    public Result<PageResult> list(ExaminationReportQueryDTO examinationReportQueryDTO){
        return Result.success(examinationReportService.list(examinationReportQueryDTO));
    }
}
