package com.universe.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.universe.common.Result;
import com.universe.dto.MovieDetailDTO;
import com.universe.user.dto.UserFavoriteDTO;
import com.universe.user.entity.UserFavorite;
import com.universe.user.feign.MovieFeignClient;
import com.universe.user.mapper.UserFavoriteMapper;
import com.universe.user.service.UserFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收藏服务实现类
 *
 * 🔥 核心功能：
 * 1. 添加/删除收藏
 * 2. 通过 OpenFeign 调用 movie-service 获取电影详情
 * 3. 组装返回数据给前端
 *
 * @author mac
 * @description 针对表【user_favorite(用户收藏表)】的数据库操作Service实现
 * @createDate 2025-12-08 15:30:38
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite>
        implements UserFavoriteService {

    /**
     * ✅ 注入 Feign 客户端（用于调用 movie-service）
     *
     * 使用 @RequiredArgsConstructor 自动注入 final 字段
     */
    private final MovieFeignClient movieFeignClient;

    /**
     * 添加收藏
     *
     * 🎯 功能：
     * 1. 检查是否已收藏（防止重复）
     * 2. 调用 movie-service 验证电影是否存在
     * 3. 保存收藏记录到数据库
     *
     * @param userId 用户ID
     * @param movieId 电影ID
     * @throws RuntimeException 如果电影已收藏或不存在
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long movieId) {
        log.info("🎬 用户 {} 收藏电影 {}", userId, movieId);

        // ========== 1. 检查是否已经收藏 ==========
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getMovieId, movieId);

        Long count = this.baseMapper.selectCount(wrapper);
        if (count > 0) {
            log.warn("❌ 用户 {} 已收藏过电影 {}", userId, movieId);
            throw new RuntimeException("已经收藏过该电影");
        }

        // ========== 2. 调用 movie-service 验证电影是否存在 ==========
        try {
            log.info("📡 调用 movie-service 验证电影是否存在，movieId: {}", movieId);

            Result<MovieDetailDTO> result = movieFeignClient.getMovieDetail(movieId);

            // 检查响应结果
            if (result == null || result.getCode() != 200 || result.getData() == null) {
                log.error("❌ 电影不存在或已下架，movieId: {}, result: {}", movieId, result);
                throw new RuntimeException("电影不存在或已下架");
            }

            MovieDetailDTO movieDetail = result.getData();
            log.info("✅ 电影验证成功: {} (ID: {})", movieDetail.getTitle(), movieId);

        } catch (Exception e) {
            log.error("❌ 调用电影服务失败，movieId: {}", movieId, e);
            throw new RuntimeException("收藏失败：电影服务异常 - " + e.getMessage());
        }

        // ========== 3. 保存收藏记录 ==========
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setMovieId(movieId);
        favorite.setCreateTime(new Date());

        boolean saved = this.save(favorite);
        if (!saved) {
            log.error("❌ 保存收藏记录失败，userId: {}, movieId: {}", userId, movieId);
            throw new RuntimeException("保存收藏记录失败");
        }

        log.info("✅ 收藏成功，userId: {}, movieId: {}", userId, movieId);
    }

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param movieId 电影ID
     * @throws RuntimeException 如果未找到收藏记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long movieId) {
        log.info("🗑️ 用户 {} 取消收藏电影 {}", userId, movieId);

        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getMovieId, movieId);

        boolean removed = this.remove(wrapper);
        if (!removed) {
            log.warn("❌ 未找到收藏记录，userId: {}, movieId: {}", userId, movieId);
            throw new RuntimeException("未找到收藏记录");
        }

        log.info("✅ 取消收藏成功，userId: {}, movieId: {}", userId, movieId);
    }

    /**
     * 🔥 获取用户收藏的电影列表（带电影详细信息）
     *
     * ⚠️ 核心逻辑：
     * 1. 查询 user_favorite 表，获取用户的所有收藏记录
     * 2. 遍历收藏记录，通过 OpenFeign 批量调用 movie-service 获取电影详情
     * 3. 将收藏记录信息 + 电影详情组装成 UserFavoriteDTO
     * 4. 返回给前端展示
     *
     * 🎯 场景：用户进入"我的收藏"页面
     *
     * @param userId 用户ID
     * @return 收藏列表（包含电影详情）
     */
    @Override
    public List<UserFavoriteDTO> getFavoriteMoviesWithDetails(Long userId) {
        log.info("📋 获取用户 {} 的收藏列表（带电影详情）", userId);

        // ========== 1. 查询用户的收藏记录 ==========
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .orderByDesc(UserFavorite::getCreateTime);  // 按收藏时间倒序

        List<UserFavorite> favorites = this.list(wrapper);

        if (favorites == null || favorites.isEmpty()) {
            log.info("ℹ️ 用户 {} 暂无收藏记录", userId);
            return new ArrayList<>();
        }

        log.info("📊 查询到 {} 条收藏记录", favorites.size());

        // ========== 2. 批量获取电影详细信息 ==========
        List<UserFavoriteDTO> result = new ArrayList<>();

        for (UserFavorite favorite : favorites) {
            try {
                log.info("📡 调用 movie-service 获取电影详情，movieId: {}", favorite.getMovieId());

                // ✅ 通过 Feign 调用 movie-service
                Result<MovieDetailDTO> movieResult =
                        movieFeignClient.getMovieDetail(favorite.getMovieId());

                // 检查响应结果
                if (movieResult != null && movieResult.getCode() == 200 && movieResult.getData() != null) {
                    MovieDetailDTO movieDetail = movieResult.getData();

                    // ========== 3. 组装 DTO ==========
                    UserFavoriteDTO dto = new UserFavoriteDTO();

                    // 设置收藏记录信息
                    dto.setFavoriteId(favorite.getId());
                    dto.setUserId(favorite.getUserId());
                    dto.setCreateTime(favorite.getCreateTime());

                    // 复制电影详细信息（使用 Spring 的 BeanUtils）
                    BeanUtils.copyProperties(movieDetail, dto);

                    result.add(dto);

                    log.info("✅ 成功获取电影详情: {} (ID: {})", movieDetail.getTitle(), favorite.getMovieId());

                } else {
                    // 电影不存在或已下架，记录警告但不中断流程
                    log.warn("⚠️ 电影 {} 不存在或已下架，跳过", favorite.getMovieId());
                }

            } catch (Exception e) {
                // 单个电影获取失败，记录错误但继续处理其他电影
                log.error("❌ 获取电影 {} 详情失败: {}", favorite.getMovieId(), e.getMessage(), e);
                // 继续处理其他电影，不中断整个流程
            }
        }

        log.info("✅ 成功获取 {} 条收藏记录（含电影详情）", result.size());
        return result;
    }

    /**
     * 检查用户是否收藏了某部电影
     *
     * 🎯 场景：用户进入电影详情页，判断是否已收藏（显示"收藏"或"已收藏"按钮）
     *
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return true-已收藏，false-未收藏
     */
    @Override
    public boolean isFavorite(Long userId, Long movieId) {
        log.info("🔍 检查用户 {} 是否收藏电影 {}", userId, movieId);

        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getMovieId, movieId);

        Long count = this.count(wrapper);
        boolean isFavorite = count > 0;

        log.info("📊 检查结果: userId={}, movieId={}, isFavorite={}", userId, movieId, isFavorite);
        return isFavorite;
    }

    /**
     * 获取用户收藏的电影ID列表（不包含电影详情）
     *
     * 🎯 场景：
     * 1. 批量检查收藏状态
     * 2. 前端缓存用户收藏的电影ID
     *
     * @param userId 用户ID
     * @return 电影ID列表
     */
    @Override
    public List<Long> getFavoriteMovieIds(Long userId) {
        log.info("📋 获取用户 {} 的收藏电影ID列表", userId);

        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .select(UserFavorite::getMovieId)  // 只查询 movieId 字段
                .orderByDesc(UserFavorite::getCreateTime);

        List<Long> movieIds = this.list(wrapper).stream()
                .map(UserFavorite::getMovieId)
                .collect(Collectors.toList());

        log.info("✅ 成功获取 {} 个电影ID", movieIds.size());
        return movieIds;
    }

    /**
     * 🆕 根据原有接口保留的方法
     *
     * @param userId 用户ID
     * @return 收藏记录列表（不含电影详情）
     */
    @Override
    public List<UserFavorite> getFavoriteMovies(Long userId) {
        log.info("📋 获取用户 {} 的收藏记录（不含电影详情）", userId);

        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId)
                .orderByDesc(UserFavorite::getCreateTime);

        List<UserFavorite> favorites = this.list(wrapper);

        log.info("✅ 查询到 {} 条收藏记录", favorites != null ? favorites.size() : 0);
        return favorites;
    }
}