package com.tyut.controller.admin;

import com.tyut.dto.AddDoctorDTO;
import com.tyut.dto.DoctorScheduleUpdateDTO;
import com.tyut.result.Result;
import com.tyut.service.DoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "管理员医生管理接口")
@RequestMapping("/admin/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @ApiOperation("新增医生")
    @PostMapping
    public Result<String> addDoctor(@RequestBody AddDoctorDTO addDoctorDTO) {
        doctorService.registerDoctor(addDoctorDTO);
        return Result.success();
    }
    @ApiOperation("医生排班")
    @PutMapping("/schedule")
    public Result<String> doctorSchedule(@RequestBody DoctorScheduleUpdateDTO dto) {
        doctorService.doctorSchedule(dto);
        return Result.success();
    }
}
