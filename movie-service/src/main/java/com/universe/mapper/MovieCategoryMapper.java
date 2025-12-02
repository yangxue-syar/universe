package com.universe.mapper;

import com.universe.entity.MovieCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mac
* @description 针对表【movie_category(电影分类表)】的数据库操作Mapper
* @createDate 2025-11-30 14:42:11
* @Entity com.universe.entity.movie_category
*/
@Mapper
public interface MovieCategoryMapper extends BaseMapper<MovieCategory> {

}




