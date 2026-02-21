package com.tyut.controller.doctor;

import com.tyut.dto.UpdateProfileDTO;
import com.tyut.result.Result;
import com.tyut.service.UserService;
import com.tyut.vo.DoctorDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("doctorAccountController")
@RequestMapping("/doctor/account")
@Api(tags="医生账号接口")
public class AccountController {
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public Result<DoctorDetailVO> getDoctorDetailById(@PathVariable Long id){
        return Result.success(userService.getDoctorById(id));
    }
    @PutMapping
    @ApiOperation("修改医生信息")
    public Result<String> updateDoctor(@RequestBody UpdateProfileDTO updateProfileDTO){
        userService.updateDoctor(updateProfileDTO);
        return Result.success();
    }
}
