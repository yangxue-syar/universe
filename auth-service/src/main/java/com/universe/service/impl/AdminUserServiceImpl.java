package com.universe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.universe.service.AdminUserService;
import com.universe.user.dto.AssignRoleDTO;
import com.universe.user.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    @Override
    public IPage<SysUser> listUsers(Integer page, Integer size, String keywords) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        return null;
    }

    @Override
    public void crrateUser(SysUser user) {

    }

    @Override
    public void updateUser(SysUser user) {

    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public void changeUserStatus(Long id, Integer status) {

    }

    @Override
    public void assignRole(AssignRoleDTO dto) {

    }
}
