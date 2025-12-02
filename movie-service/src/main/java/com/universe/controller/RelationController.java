package com.universe.controller;

import com.universe.common.Result;
import com.universe.service.MovieCategoryRelationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/relation")
public class RelationController {
    @Resource
    private MovieCategoryRelationService movieCategoryRelationService;

    /**
     * 绑定电影和分类的关系
     *
     * @param movieId 电影ID
     * @param categoryIds 分类ID列表
     * @return 操作结果
     */
    @PostMapping("/bind")
    public Result<String> bind(
            @RequestParam Long movieId,
            @RequestBody List<Long> categoryIds) {


        log.info("绑定电影分类，电影ID: {}, 分类IDs: {}", movieId, categoryIds);

        try {
            movieCategoryRelationService.bind(movieId, categoryIds);
           log.info("成功绑定电影分类");
            return Result.success("绑定成功");

        } catch (Exception e) {
            log.info("绑定电影分类失败", e);
            return Result.error(500, "绑定失败: " + e.getMessage());
        }
    }

    /**
     * 解绑电影和分类的关系
     *
     * @param movieId 电影ID
     * @param categoryId 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/unbind")
    public Result<String> unbind(
            @RequestParam Long movieId,
            @RequestParam Long categoryId) {
        log.info("解绑电影分类，电影ID: {}, 分类ID: {}", movieId, categoryId);

        try {
            movieCategoryRelationService.unbind(movieId, categoryId);
            log.info("成功解绑电影分类");
            return Result.success("解绑成功");
        } catch (Exception e) {
            log.error("解绑电影分类失败", e);
            return Result.error(500, "解绑失败: " + e.getMessage());
        }
    }

    /**
     * 获取电影的所有分类
     *
     * @param movieId 电影ID
     * @return 分类ID列表
     */
    @GetMapping("/movie/{movieId}")
    public Result<List<Long>> getCategoriesByMovie(@PathVariable Long movieId) {
       log.info("📋 获取电影的分类，电影ID: {}", movieId);

        try {
            // 假设你有这个方法，如果没有需要在 Service 中添加
            // List<Long> categoryIds = movieCategoryRelationService.getCategoryIdsByMovieId(movieId);
            // logger.info("✅ 成功获取电影分类，数量: {}", categoryIds.size());
            // return Result.success(categoryIds);

            return Result.error(501, "功能未实现");

        } catch (Exception e) {
          log.info("获取电影分类失败", e);
            return Result.error(500, "获取电影分类失败: " + e.getMessage());
        }
    }
}
