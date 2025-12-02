package com.universe.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.universe.dto.CategoryTreeDTO;
import com.universe.entity.MovieCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mac
* @description 针对表【movie_category(电影分类表)】的数据库操作Service
* @createDate 2025-11-30 14:42:11
*/

public interface MovieCategoryService extends IService<MovieCategory> {
    List<CategoryTreeDTO> getCategoryTree();
}
