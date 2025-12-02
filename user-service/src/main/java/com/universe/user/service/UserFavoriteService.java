package com.universe.user.service;

import com.universe.user.dto.UserFavoriteDTO;
import com.universe.user.entity.UserFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户收藏服务接口
 *
 * @author mac
 * @description 针对表【user_favorite(用户收藏表)】的数据库操作Service
 * @createDate 2025-12-08 15:30:38
 */
public interface UserFavoriteService extends IService<UserFavorite> {

    /**
     * 添加收藏
     *
     * @param userId 用户ID
     * @param movieId 电影ID
     */
    void addFavorite(Long userId, Long movieId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param movieId 电影ID
     */
    void removeFavorite(Long userId, Long movieId);

    /**
     * ✅ 获取用户收藏的电影列表（带电影详细信息）
     *
     * 🔥 核心功能：
     * 1. 查询 user_favorite 表获取收藏记录
     * 2. 通过 OpenFeign 调用 movie-service 获取每部电影的详细信息
     * 3. 组装成 UserFavoriteDTO 返回给前端
     *
     * @param userId 用户ID
     * @return 收藏列表（包含电影详情）
     */
    List<UserFavoriteDTO> getFavoriteMoviesWithDetails(Long userId);

    /**
     * 检查用户是否收藏了某部电影
     *
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return true-已收藏，false-未收藏
     */
    boolean isFavorite(Long userId, Long movieId);

    /**
     * 获取用户收藏的电影ID列表（不包含电影详情）
     *
     * @param userId 用户ID
     * @return 电影ID列表
     */
    List<Long> getFavoriteMovieIds(Long userId);

    List<UserFavorite> getFavoriteMovies(Long userId);
}