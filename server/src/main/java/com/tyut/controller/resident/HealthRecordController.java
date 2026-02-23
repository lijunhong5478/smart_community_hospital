package com.tyut.controller.resident;

import com.tyut.dto.HealthRecordQueryDTO;
import com.tyut.entity.MedicalVisit;
import com.tyut.mapper.MedicalVisitMapper;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.HealthRecordService;
import com.tyut.service.MedicalVisitService;
import com.tyut.vo.HealthRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("residentHealthRecordController")
@RequestMapping("/resident/healthRecord")
@Api(tags="居民健康档案接口")
public class HealthRecordController {
    @Autowired
    private HealthRecordService healthRecordService;
    @Autowired
    private MedicalVisitService medicalVisitService;

    @GetMapping("/{id}")
    @ApiOperation("根据id查询健康档案")
    public Result<PageResult> getHealthRecordById(@PathVariable Long id){
        HealthRecordQueryDTO dto=HealthRecordQueryDTO.builder()
                .userId(id)
                .pageNum(1)
                .pageSize(1)
                .build();
        return Result.success(healthRecordService.list(dto));
    }
    @GetMapping("/medicalVisit/{id}")
    @ApiOperation("根据visitId查询用户的详细问诊记录")
    public Result<MedicalVisit> getMedicalVisitById(@PathVariable Long id){
        return Result.success(medicalVisitService.getById(id));
    }
}
