package com.tyut.controller.resident;

import com.tyut.dto.MedicalVisitQueryDTO;
import com.tyut.entity.MedicalVisit;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.MedicalVisitService;
import com.tyut.vo.MedicalVisitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("residentMedicalVisitController")
@Api(tags="社区居民问诊接口")
@RequestMapping("/resident/medicalVisit")
public class MedicalVisitController {
    @Autowired
    private MedicalVisitService medicalVisitService;
    @GetMapping("/{id}")
    @ApiOperation("根据id查询问诊信息")
    public Result<MedicalVisitVO> getMedicalVisit(@PathVariable Long id){
        return Result.success(medicalVisitService.getById(id));
    }
    @GetMapping("/list")
    @ApiOperation("查询问诊列表")
    public Result<PageResult> listMedicalVisit(MedicalVisitQueryDTO medicalVisitQueryDTO){
        return Result.success(medicalVisitService.list(medicalVisitQueryDTO));
    }
}
