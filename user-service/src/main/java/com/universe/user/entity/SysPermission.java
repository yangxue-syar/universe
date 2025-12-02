package com.universe.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 权限表
 * @TableName sys_permission
 */
@TableName(value ="sys_permission")
public class SysPermission {
    /**
     * 权限ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限字符串
     */
    private String permissionKey;

    /**
     * 权限类型(1菜单,2按钮)
     */
    private Integer type;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(0禁用,1启用)
     */
    private Integer status;

    /**
     * 权限ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 权限ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 父权限ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 父权限ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 权限名称
     */
    public String getName() {
        return name;
    }

    /**
     * 权限名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 权限字符串
     */
    public String getPermissionKey() {
        return permissionKey;
    }

    /**
     * 权限字符串
     */
    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    /**
     * 权限类型(1菜单,2按钮)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 权限类型(1菜单,2按钮)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 路由路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 路由路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 组件路径
     */
    public String getComponent() {
        return component;
    }

    /**
     * 组件路径
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 状态(0禁用,1启用)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态(0禁用,1启用)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysPermission other = (SysPermission) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPermissionKey() == null ? other.getPermissionKey() == null : this.getPermissionKey().equals(other.getPermissionKey()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getComponent() == null ? other.getComponent() == null : this.getComponent().equals(other.getComponent()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPermissionKey() == null) ? 0 : getPermissionKey().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getComponent() == null) ? 0 : getComponent().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", parentId=").append(parentId);
        sb.append(", name=").append(name);
        sb.append(", permissionKey=").append(permissionKey);
        sb.append(", type=").append(type);
        sb.append(", path=").append(path);
        sb.append(", component=").append(component);
        sb.append(", icon=").append(icon);
        sb.append(", sort=").append(sort);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}