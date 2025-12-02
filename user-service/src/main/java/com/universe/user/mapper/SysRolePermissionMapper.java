package com.universe.user.mapper;

import com.universe.user.entity.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mac
* @description 针对表【sys_role_permission(角色权限关联表)】的数据库操作Mapper
* @createDate 2025-12-08 15:30:17
* @Entity com.universe.user.entity.SysRolePermission
*/
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

}




