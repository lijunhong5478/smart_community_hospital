package com.tyut.controller.resident;

import com.tyut.dto.DoctorQueryDTO;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.DoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("residentDoctorController")
@RequestMapping("/resident/doctor")
@Api(tags = "居民-医生信息接口")
@Slf4j
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @GetMapping("/list")
    @ApiOperation("社区居民查询医生信息")
    public Result<PageResult> list(DoctorQueryDTO doctorQueryDTO){
        log.info("查询医生信息:{}",doctorQueryDTO);
        return Result.success(doctorService.list(doctorQueryDTO));
    }

}
