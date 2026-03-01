package com.tyut.controller.resident;

import com.tyut.entity.PhysicalExamRecord;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.PhysicalExamRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("residentPhysicalExamRecordController")
@RequestMapping("/resident/physicalExamRecord")
@Api(tags = "居民体检记录查询接口")
public class PhysicalExamRecordController {

    @Autowired
    private PhysicalExamRecordService physicalExamRecordService;

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询体检记录")
    public Result<PhysicalExamRecord> getById(@PathVariable Long id) {
        return Result.success(physicalExamRecordService.getById(id));
    }

    @GetMapping("/list")
    @ApiOperation("居民查询自己的体检记录列表")
    public Result<PageResult> listByResident(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long recordId) {
        return Result.success(physicalExamRecordService.listByResident(pageNum, pageSize, recordId));
    }

    @GetMapping("/latest")
    @ApiOperation("查询最近一次的体检记录")
    public Result<PhysicalExamRecord> getLatestByResident(@RequestParam Long recordId) {
        return Result.success(physicalExamRecordService.getLatestByResident(recordId));
    }
}
