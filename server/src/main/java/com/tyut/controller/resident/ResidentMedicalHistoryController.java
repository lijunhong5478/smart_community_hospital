package com.tyut.controller.resident;

import com.tyut.entity.ResidentMedicalHistory;
import com.tyut.result.Result;
import com.tyut.service.ResidentMedicalHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("residentResidentMedicalHistoryController")
@RequestMapping("/resident/residentMedicalHistory")
@Api(tags = "居民病史查询接口")
public class ResidentMedicalHistoryController {

    @Autowired
    private ResidentMedicalHistoryService residentMedicalHistoryService;

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询病史记录")
    public Result<ResidentMedicalHistory> getById(@PathVariable Long id) {
        return Result.success(residentMedicalHistoryService.getById(id));
    }

    @GetMapping
    @ApiOperation("居民查询自己的病史记录")
    public Result<ResidentMedicalHistory> getByResident(@RequestParam Long recordId) {
        return Result.success(residentMedicalHistoryService.getByResident(recordId));
    }
}
