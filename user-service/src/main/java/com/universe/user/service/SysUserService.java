package com.universe.user.service;

import com.universe.user.dto.UserLoginDTO;
import com.universe.user.dto.UserRegisterDTO;
import com.universe.user.dto.UserUpdateDTO;
import com.universe.user.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.universe.user.vo.UserProfileVO;

import java.util.List;

/**
* @author mac
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-12-08 15:30:26
*/
/**
 * 系统用户服务接口
 * 提供用户注册、登录、权限管理等核心功能
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户注册功能
     * @param dto 用户注册信息数据传输对象，包含用户名、密码等注册必要信息
     */
    void register(UserRegisterDTO dto);

    /**
     * 用户登录功能
     * @param dto 用户登录信息数据传输对象，包含用户名和密码
     * @return 返回登录成功后的认证令牌字符串
     */
    String login(UserLoginDTO dto);

    /**
     * 获取用户个人信息
     * @param userId 用户唯一标识符
     * @return 返回指定用户的详细信息对象
     */
    SysUser getProfile(String userId);

    /**
     * 获取用户角色列表
     * @param userId 用户唯一标识符
     * @return 返回用户拥有的所有角色名称列表
     */
    List<String> getRoles(Long userId);

    /**
     * 获取用户权限列表
     * @param userId 用户唯一标识符
     * @return 返回用户拥有的所有权限标识符列表
     */
    List<String> getPermissions(Long userId);

    UserProfileVO getProfiles(String userId);
    /**
     * 更新用户信息
     * @param userId 用户唯一标识符
     * @param dto 用户更新信息数据传输对象
     */
    void updateUserInfo(Long userId, UserUpdateDTO dto);


}
