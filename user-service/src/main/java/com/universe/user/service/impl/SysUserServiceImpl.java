package com.universe.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.universe.common.utils.JwtUtils;
import com.universe.common.utils.PasswordUtils;
import com.universe.user.dto.UserLoginDTO;
import com.universe.user.dto.UserRegisterDTO;
import com.universe.user.dto.UserUpdateDTO;
import com.universe.user.entity.SysUser;
import com.universe.user.entity.UserFavorite;
import com.universe.user.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.universe.user.mapper.UserFavoriteMapper;
import com.universe.user.service.SysUserService;
import com.universe.user.vo.UserProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZoneId;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Override
    public void register(UserRegisterDTO dto) {
        if(StringUtils.hasText(dto.getUsername())) {
            SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", dto.getUsername()));
            if(user!= null) throw new RuntimeException("用户名已存在");
        }
        if(StringUtils.hasText(dto.getEmail())) {
            SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("email", dto.getEmail()));
            if(user!= null) throw new RuntimeException("邮箱已存在");
        }
        if(StringUtils.hasText(dto.getPhone())) {
            SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("phone", dto.getPhone()));
            if(user!= null) throw new RuntimeException("手机号已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStatus(1);
        this.save(user);
    }

    @Override
    public String login(UserLoginDTO dto) {
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", dto.getUsername()));
        if (user == null) throw new RuntimeException("用户不存在");
        if (!PasswordUtils.matches(dto.getPassword(), user.getPassword())) throw new RuntimeException("用户名或密码错误");
        if (user.getStatus() != null && user.getStatus() == 0) throw new RuntimeException("账号被禁用");

        // create JWT
        return JwtUtils.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public SysUser getProfile(String userId) {
        // 实现获取用户个人资料逻辑
        return baseMapper.selectById(userId);
    }

    @Override
    public List<String> getRoles(Long userId) {
        return sysUserMapper.getSysUserRoles(userId);
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return sysUserMapper.getSysUserPermissions(userId);
    }
    @Override
    public UserProfileVO getProfiles(String userId) {
        // 查询用户基本信息
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 构建返回对象
        UserProfileVO profile = new UserProfileVO();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setNickname(user.getNickname());
        profile.setAvatar(user.getAvatar());
        profile.setEmail(user.getEmail());
        profile.setPhone(user.getPhone());
        profile.setGender(user.getGender());
        profile.setStatus(user.getStatus());
        profile.setIsAdmin(user.getIsAdmin());
        // 如果你选择在赋值时做类型转换（假设 user.getCreateTime() 返回的是 java.util.Date）
        profile.setCreateTime(user.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        profile.setUpdateTime(user.getUpdateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        // 查询收藏数量
        LambdaQueryWrapper<UserFavorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(UserFavorite::getUserId, userId);
        Long favoriteCount = userFavoriteMapper.selectCount(favoriteWrapper);
        profile.setFavoriteCount(favoriteCount != null ? favoriteCount.intValue() : 0);

        // TODO: 查询评论数量（需要评论表）
        profile.setCommentCount(0);

        // TODO: 查询观看历史数量（需要历史记录表）
        profile.setWatchHistoryCount(0);

        return profile;
    }

    @Override
    public void updateUserInfo(Long userId, UserUpdateDTO dto) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 检查用户名是否重复（如果要修改用户名）
        if (StringUtils.hasText(dto.getUsername()) && !dto.getUsername().equals(user.getUsername())) {
            SysUser existUser = sysUserMapper.selectOne(
                    new QueryWrapper<SysUser>()
                            .eq("username", dto.getUsername())
                            .ne("id", userId)
            );
            if (existUser != null) {
                throw new RuntimeException("用户名已被使用");
            }
            user.setUsername(dto.getUsername());
        }

        // 3. 检查手机号是否重复（如果要修改手机号）
        if (StringUtils.hasText(dto.getPhone()) && !dto.getPhone().equals(user.getPhone())) {
            SysUser existUser = sysUserMapper.selectOne(
                    new QueryWrapper<SysUser>()
                            .eq("phone", dto.getPhone())
                            .ne("id", userId)
            );
            if (existUser != null) {
                throw new RuntimeException("手机号已被使用");
            }
            user.setPhone(dto.getPhone());
        }

        // 4. 检查邮箱是否重复（如果要修改邮箱）
        if (StringUtils.hasText(dto.getEmail()) && !dto.getEmail().equals(user.getEmail())) {
            SysUser existUser = sysUserMapper.selectOne(
                    new QueryWrapper<SysUser>()
                            .eq("email", dto.getEmail())
                            .ne("id", userId)
            );
            if (existUser != null) {
                throw new RuntimeException("邮箱已被使用");
            }
            user.setEmail(dto.getEmail());
        }

        // 5. 修改密码（需要验证旧密码）
        if (StringUtils.hasText(dto.getNewPassword())) {
            if (!StringUtils.hasText(dto.getOldPassword())) {
                throw new RuntimeException("修改密码需要提供旧密码");
            }
            // 验证旧密码
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("旧密码不正确");
            }
            // 设置新密码
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        // 6. 更新其他信息
        if (StringUtils.hasText(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (StringUtils.hasText(dto.getAvatar())) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getGender() != null) {
            // 验证性别值是否合法 (0:未知, 1:男, 2:女)
            if (dto.getGender() < 0 || dto.getGender() > 2) {
                throw new RuntimeException("性别参数不合法");
            }
            user.setGender(dto.getGender());
        }

        // 7. 执行更新
        boolean success = this.updateById(user);
        if (!success) {
            throw new RuntimeException("更新用户信息失败");
        }
    }
}
