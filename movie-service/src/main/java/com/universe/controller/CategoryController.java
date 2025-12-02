package com.universe.controller;

import com.universe.common.Result;
import com.universe.dto.CategoryTreeDTO;
import com.universe.service.MovieCategoryService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    @Resource
    private MovieCategoryService movieCategoryService;
    @GetMapping("/tree")
    public Result<List<CategoryTreeDTO>> tree() {
        log.info("获取类别树");
        try {
            List<CategoryTreeDTO> categories = movieCategoryService.getCategoryTree();
            log.info("成功获取类别树,数量：{}", categories.size());
            return Result.success(categories);
        } catch (Exception e) {
            log.error("获取类别树失败", e);
            return Result.error(500, "获取类别树失败: " + e.getMessage());
        }


    }
}
