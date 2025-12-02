package com.universe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.universe.user.dto.AssignRoleDTO;
import com.universe.user.entity.SysUser;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @GetMapping("/list")
    public IPage<SysUser> list(@RequestParam Integer page,
                               @RequestParam Integer size,
                               @RequestParam(required = false) String keyword) {
        return null;
    }
    @PostMapping("create")
    public String create(@RequestBody SysUser sysUser) {
        return "success";
    }

    @PutMapping("update")
    public String update(@RequestBody SysUser sysUser) {
        return "success";
    }

    @Delete("delete/{id}")
    public String delete(@PathVariable Long id) {
        return "success";
    }

    @PutMapping("changeStatus/{id}")
    public String changeStatus(@PathVariable Long id) {
        return "success";
    }

    @GetMapping("/assignRole")
    public String assignRole(@RequestBody AssignRoleDTO dto) {
        return "success";
    }

}
