package com.tyut.controller.doctor;

import com.tyut.entity.ExaminationReport;
import com.tyut.result.Result;
import com.tyut.service.ExaminationReportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor/examinationReport")
public class ExaminationReportController {
    @Autowired
    private ExaminationReportService examinationReportService;
    @PostMapping
    @ApiOperation("保存检查报告")
    public Result<String> save(@RequestBody ExaminationReport examinationReport){
        examinationReportService.save(examinationReport);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询检查报告")
    public Result<ExaminationReport> getById(@PathVariable Long id){
        return Result.success(examinationReportService.getById(id));
    }
}
