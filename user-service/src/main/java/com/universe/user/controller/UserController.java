package com.universe.user.controller;

import com.universe.common.Result;
import com.universe.user.dto.UserUpdateDTO;
import com.universe.user.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private  final SysUserService sysUserService;

    @GetMapping("/profile/{id}")
    public Result<?>  profile(@PathVariable Long id) {
        return Result.success(sysUserService.getProfile(id.toString()));
    }

    @GetMapping("/roles/{id}")
    public Result<?>  roles(@PathVariable Long id) {
        return Result.success(sysUserService.getRoles(id));
    }
    @GetMapping("/permissions/{id}")
    public Result<?>  permissions(@PathVariable Long id) {
        return Result.success(sysUserService.getPermissions(id));
    }
    /**
     * 更新用户信息
     * 从 JWT Token 中获取当前登录用户ID
     */
    @PutMapping("/update")
    public Result<?> updateUserInfo(
            @Valid @RequestBody UserUpdateDTO dto,
            HttpServletRequest request) {
        // 从 request 属性中获取 JwtFilter 设置的 userId
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录或登录已过期");
        }

        try {
            sysUserService.updateUserInfo(userId, dto);
            return Result.success("用户信息更新成功");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 管理员更新指定用户信息
     * 需要管理员权限
     */
    @PutMapping("/update/{id}")
    public Result<?> updateUserInfoById(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto) {
        try {
            sysUserService.updateUserInfo(id, dto);
            return Result.success("用户信息更新成功");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
