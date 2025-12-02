package com.universe.service;

import com.universe.entity.MovieCategoryRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mac
* @description 针对表【movie_category_relation(电影分类关联表)】的数据库操作Service
* @createDate 2025-11-30 14:41:24
*/
/**
 * 电影分类关联服务接口
 * 提供电影与分类之间的绑定和查询功能
 */
public interface MovieCategoryRelationService extends IService<MovieCategoryRelation> {

    void bind(Long movieId, List<Long> categoryIds);
    void unbind(Long movieId, Long categoryId);
    List<Long> getCategoryIdsByMovieId(Long movieId);

}

