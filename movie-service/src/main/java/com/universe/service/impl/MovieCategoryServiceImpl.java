package com.universe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.universe.dto.CategoryTreeDTO;
import com.universe.entity.MovieCategory;
import com.universe.service.MovieCategoryService;
import com.universe.mapper.MovieCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mac
 * @description 针对表【movie_category(电影分类表)】的数据库操作Service实现
 * @createDate 2025-11-30 14:42:11
 */
@Service
public class MovieCategoryServiceImpl extends ServiceImpl<MovieCategoryMapper, MovieCategory>
        implements MovieCategoryService {

    @Autowired
    private MovieCategoryMapper movieCategoryMapper;

    @Override
    public List<CategoryTreeDTO> getCategoryTree() {
        // 1. 查询所有分类
        List<MovieCategory> allCategories = movieCategoryMapper.selectList(null);

        // 2. 找出顶级分类（parent_id 为 null）
        List<MovieCategory> topCategories = allCategories.stream()
                .filter(cat -> cat.getParentId() == null)
                .sorted((a, b) -> {
                    // 按sort排序,如果sort为null则放到后面
                    Integer sortA = a.getSort() != null ? a.getSort() : Integer.MAX_VALUE;
                    Integer sortB = b.getSort() != null ? b.getSort() : Integer.MAX_VALUE;
                    return sortA.compareTo(sortB);
                })
                .collect(Collectors.toList());

        // 3. 构建树形结构
        return topCategories.stream()
                .map(cat -> buildTree(cat, allCategories))
                .collect(Collectors.toList());
    }

    /**
     * 递归构建分类树
     * @param category 当前分类节点
     * @param allCategories 所有分类列表
     * @return 分类树DTO
     */
    private CategoryTreeDTO buildTree(MovieCategory category, List<MovieCategory> allCategories) {
        CategoryTreeDTO dto = new CategoryTreeDTO();
        dto.setId(category.getId());
        dto.setName(category.getCategoryName());
        dto.setSort(category.getSort()); // 🆕 添加sort字段

        // 查找子分类
        List<CategoryTreeDTO> children = allCategories.stream()
                .filter(cat -> category.getId().equals(cat.getParentId()))
                .sorted((a, b) -> {
                    // 子分类也按sort排序
                    Integer sortA = a.getSort() != null ? a.getSort() : Integer.MAX_VALUE;
                    Integer sortB = b.getSort() != null ? b.getSort() : Integer.MAX_VALUE;
                    return sortA.compareTo(sortB);
                })
                .map(cat -> buildTree(cat, allCategories))
                .collect(Collectors.toList());

        dto.setChildren(children);
        return dto;
    }
}