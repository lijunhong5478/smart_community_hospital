package com.tyut.controller.resident;

import com.tyut.dto.ResidentRegisterDTO;
import com.tyut.dto.UpdateProfileDTO;
import com.tyut.result.Result;
import com.tyut.service.UserService;
import com.tyut.vo.ResidentDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("residentAccountController")
@RequestMapping("/resident/account")
@Api(tags="居民账号接口")
public class AccountController {
    @Autowired
    private UserService userService;
    @ApiOperation("根据Id查询")
    @GetMapping("/{id}")
    public Result<ResidentDetailVO> getResidentDetailById(@PathVariable Long id){
        return Result.success(userService.getResidentById(id));
    }
    @ApiOperation("社区居民注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody ResidentRegisterDTO residentRegisterDTO){
        userService.registerResident(residentRegisterDTO);
        return Result.success();
    }
    @ApiOperation("社区居民修改个人信息")
    @PutMapping
    public Result<String> updateResident(@RequestBody UpdateProfileDTO updateProfileDTO){
        userService.updateResident(updateProfileDTO);
        return Result.success();
    }
}
