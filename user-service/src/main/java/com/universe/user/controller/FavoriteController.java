package com.universe.user.controller;

import com.universe.common.Result;
import com.universe.user.dto.UserFavoriteDTO;
import com.universe.user.service.UserFavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户收藏控制器
 *
 * 🔥 核心功能：
 * 1. 添加/取消收藏
 * 2. 获取收藏列表（调用 movie-service 获取电影详情）
 * 3. 检查收藏状态
 *
 * ✅ JWT Token 验证：
 * - 所有接口都需要 JWT Token 验证（由 JwtFilter 拦截）
 * - 从 request.getAttribute("userId") 获取当前登录用户ID
 */
@Slf4j
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final UserFavoriteService favoriteService;

    /**
     * 添加收藏
     *
     * 🎯 场景：用户点击"收藏"按钮
     *
     * @param movieId 电影ID
     * @param request HTTP 请求（包含 userId）
     * @return 操作结果
     */
    @PostMapping("/{movieId}")
    public Result<?> add(
            @PathVariable Long movieId,
            HttpServletRequest request) {

        // ✅ 从 JWT Token 中获取当前登录用户ID
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录或登录已过期");
        }

        log.info("用户 {} 收藏电影 {}", userId, movieId);

        try {
            favoriteService.addFavorite(userId, movieId);
            return Result.success("收藏成功");
        } catch (Exception e) {
            log.error("收藏失败", e);
            return Result.error(500, e.getMessage());
        }
    }

    /**
     * 取消收藏
     *
     * 🎯 场景：用户点击"取消收藏"按钮
     *
     * @param movieId 电影ID
     * @param request HTTP 请求
     * @return 操作结果
     */
    @DeleteMapping("/{movieId}")
    public Result<?> remove(
            @PathVariable Long movieId,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录或登录已过期");
        }

        log.info("用户 {} 取消收藏电影 {}", userId, movieId);

        try {
            favoriteService.removeFavorite(userId, movieId);
            return Result.success("已取消收藏");
        } catch (Exception e) {
            log.error("取消收藏失败", e);
            return Result.error(500, e.getMessage());
        }
    }

    /**
     * 🔥 获取用户收藏列表（带电影详细信息）
     *
     * 🎯 场景：用户查看"我的收藏"页面
     *
     * ⚠️ 核心逻辑：
     * 1. 查询 user_favorite 表，获取用户的所有收藏记录
     * 2. 通过 OpenFeign 调用 movie-service，获取每部电影的详细信息
     * 3. 组装成 UserFavoriteDTO 返回给前端
     *
     * @param request HTTP 请求
     * @return 收藏列表（包含电影详情）
     */
    @GetMapping("/list")
    public Result<List<UserFavoriteDTO>> list(HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录或登录已过期");
        }

        log.info("获取用户 {} 的收藏列表", userId);

        try {
            // ✅ 调用 Service，内部会通过 Feign 调用 movie-service
            List<UserFavoriteDTO> favorites = favoriteService.getFavoriteMoviesWithDetails(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            log.error("获取收藏列表失败", e);
            return Result.error(500, "获取收藏列表失败：" + e.getMessage());
        }
    }

    /**
     * 检查是否已收藏
     *
     * 🎯 场景：用户进入电影详情页，判断是否已收藏该电影（显示收藏/已收藏按钮）
     *
     * @param movieId 电影ID
     * @param request HTTP 请求
     * @return { "isFavorite": true/false }
     */
    @GetMapping("/check/{movieId}")
    public Result<Map<String, Boolean>> checkFavorite(
            @PathVariable Long movieId,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录或登录已过期");
        }

        log.info("检查用户 {} 是否收藏电影 {}", userId, movieId);

        try {
            boolean isFavorite = favoriteService.isFavorite(userId, movieId);

            Map<String, Boolean> result = new HashMap<>();
            result.put("isFavorite", isFavorite);

            return Result.success(result);
        } catch (Exception e) {
            log.error("检查收藏状态失败", e);
            return Result.error(500, "检查收藏状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户收藏的电影ID列表（不包含详情）
     *
     * @param request HTTP 请求
     * @return 电影ID列表
     */
    @GetMapping("/ids")
    public Result<List<Long>> getFavoriteIds(HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "未登录或登录已过期");
        }

        log.info("获取用户 {} 的收藏电影ID列表", userId);

        try {
            List<Long> movieIds = favoriteService.getFavoriteMovieIds(userId);
            return Result.success(movieIds);
        } catch (Exception e) {
            log.error("获取收藏ID列表失败", e);
            return Result.error(500, "获取收藏ID列表失败：" + e.getMessage());
        }
    }
}