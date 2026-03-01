package com.tyut.controller.doctor;

import com.tyut.entity.PhysicalExamRecord;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.PhysicalExamRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("doctorPhysicalExamRecordController")
@RequestMapping("/doctor/physicalExamRecord")
@Api(tags = "医生-体检记录接口")
public class PhysicalExamRecordController {

    @Autowired
    private PhysicalExamRecordService physicalExamRecordService;

    @PostMapping
    @ApiOperation("医生新增体检记录")
    public Result<String> save(@RequestBody PhysicalExamRecord physicalExamRecord) {
        physicalExamRecordService.saveByDoctor(physicalExamRecord);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询体检记录")
    public Result<PhysicalExamRecord> getById(@PathVariable Long id) {
        return Result.success(physicalExamRecordService.getById(id));
    }

    @GetMapping("/list")
    @ApiOperation("医生查询体检记录列表")
    public Result<PageResult> listByDoctor(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long recordId) {
        return Result.success(physicalExamRecordService.listByDoctor(pageNum, pageSize, recordId));
    }
}
