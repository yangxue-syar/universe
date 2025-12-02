package com.universe.mapper;

import com.universe.entity.MovieCategoryRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author mac
* @description 针对表【movie_category_relation(电影分类关联表)】的数据库操作Mapper
* @createDate 2025-11-30 14:41:24
* @Entity com.universe.entity.movie_category_relation
*/
@Mapper
public interface MovieCategoryRelationMapper extends BaseMapper<MovieCategoryRelation> {
    void deleteByMovieIdAndCategoryId(@Param("movieId") Long movieId, @Param("categoryId") Long categoryId);


}




