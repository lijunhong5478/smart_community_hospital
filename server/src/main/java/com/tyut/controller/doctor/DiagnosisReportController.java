package com.tyut.controller.doctor;

import com.tyut.dto.DiagnosisReportQueryDTO;
import com.tyut.entity.DiagnosisReport;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.DiagnosisReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("doctorDiagnosisReportController")
@RequestMapping("/doctor/diagnosisReport")
@Api(tags="医生诊断报告接口")
public class DiagnosisReportController {
    @Autowired
    private DiagnosisReportService diagnosisReportService;
    @PostMapping
    @ApiOperation("保存诊断报告")
    public Result<String> save(DiagnosisReport diagnosisReport){
        diagnosisReportService.save(diagnosisReport);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询诊断报告")
    public Result<DiagnosisReport> getById(@PathVariable Long id){
        return Result.success(diagnosisReportService.getById(id));
    }
    @GetMapping("/list")
    @ApiOperation("查询诊断报告列表")
    public Result<PageResult> list(DiagnosisReportQueryDTO diagnosisReportQueryDTO){
        return Result.success(diagnosisReportService.list(diagnosisReportQueryDTO));
    }
    @PutMapping
    @ApiOperation("修改诊断报告")
    public Result<String> update(DiagnosisReport diagnosisReport){
        diagnosisReportService.update(diagnosisReport);
        return Result.success();
    }

    /**
     * 根据诊断记录的id判断是否该医生有权修改
     * @param diagnosisId,doctorId
     * @return
     */
    @GetMapping("/check")
    @ApiOperation("检查权限")
    public Result<Boolean> check(Long diagnosisId,Long doctorId){
        return Result.success(diagnosisReportService.check(diagnosisId,doctorId));
    }

}
