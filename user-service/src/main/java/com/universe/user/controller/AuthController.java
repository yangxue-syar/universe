package com.universe.user.controller;

import com.universe.common.Result;
import com.universe.user.dto.UserLoginDTO;
import com.universe.user.dto.UserRegisterDTO;
import com.universe.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SysUserService sysUserService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody UserRegisterDTO dto) {
        sysUserService.register(dto);
        return Result.success("注册成功");
    }
    @PostMapping("/login")
    public Result<?>  login(@RequestBody UserLoginDTO dto) {
        return Result.success(sysUserService.login(dto));
    }
}
