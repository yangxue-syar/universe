package com.universe.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.universe.dto.MovieQueryDTO;
import com.universe.entity.MovieInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author mac
* @description 针对表【movie_info(电影信息表)】的数据库操作Mapper
* @createDate 2025-11-30 14:42:32
* @Entity com.universe.entity.movie_info
*/
@Mapper
public interface MovieInfoMapper extends BaseMapper<MovieInfo> {
    /**
     * 根据分类ID查询电影ID列表
     * @param categoryId 分类ID
     * @return 电影ID列表
     */
    @Select("SELECT movie_id FROM movie_category_relation WHERE category_id = #{categoryId}")
    List<Long> selectMovieIdsByCategoryId(Long categoryId);
    /**
     * 根据分类ID分页查询电影
     */
    IPage<MovieInfo> selectPageByCategory(Page<MovieInfo> page, @Param("dto") MovieQueryDTO dto);

}




