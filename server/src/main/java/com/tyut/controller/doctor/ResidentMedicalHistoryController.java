package com.tyut.controller.doctor;

import com.tyut.entity.ResidentMedicalHistory;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.ResidentMedicalHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("doctorResidentMedicalHistoryController")
@RequestMapping("/doctor/residentMedicalHistory")
@Api(tags = "医生-居民病史接口")
public class ResidentMedicalHistoryController {

    @Autowired
    private ResidentMedicalHistoryService residentMedicalHistoryService;

    @PostMapping
    @ApiOperation("医生新增居民病史记录")
    public Result<String> save(@RequestBody ResidentMedicalHistory medicalHistory) {
        residentMedicalHistoryService.saveByDoctor(medicalHistory);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询病史记录")
    public Result<ResidentMedicalHistory> getById(@PathVariable Long id) {
        return Result.success(residentMedicalHistoryService.getById(id));
    }

    @GetMapping("/list")
    @ApiOperation("医生查询病史记录列表")
    public Result<PageResult> listByDoctor(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long recordId) {
        return Result.success(residentMedicalHistoryService.listByDoctor(pageNum, pageSize, recordId));
    }
}
