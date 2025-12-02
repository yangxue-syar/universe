package com.universe.user.mapper;

import com.universe.user.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("SELECT role_name FROM sys_user_role WHERE user_id = #{userId}")
    List<String> getSysUserRoles(Long userId);
    @Select("SELECT permission_name FROM sys_user_permission WHERE user_id = #{userId}")
    List<String> getSysUserPermissions(Long userId);
}