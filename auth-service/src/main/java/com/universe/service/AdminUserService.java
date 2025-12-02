package com.universe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.universe.user.dto.AssignRoleDTO;
import com.universe.user.entity.SysUser;

public interface AdminUserService{
    IPage<SysUser> listUsers(Integer page, Integer size,String keywords);
    void crrateUser(SysUser user);
    void updateUser(SysUser user);
    void deleteUser(Long id);
    void changeUserStatus(Long id, Integer status);
    void assignRole(AssignRoleDTO dto);

}
