package com.universe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.universe.dto.MovieDetailDTO;
import com.universe.dto.MovieQueryDTO;
import com.universe.entity.MovieInfo;

import java.util.List;

/**
 * 电影信息服务接口
 */
public interface MovieInfoService extends IService<MovieInfo> {

    /**
     * 获取热门电影列表（按评分和观看次数排序）
     *
     * @param limit 返回数量
     * @return 热门电影列表
     */
    List<MovieInfo> listHot(int limit);

    /**
     * 获取最新上映的电影列表（按上映日期降序）
     *
     * @param limit 返回数量
     * @return 最新电影列表
     */
    List<MovieInfo> getLatestMovies(int limit);

        /**
         * 获取最新电影（按上映日期降序）
         */
        List<MovieInfo> getLatestMovies(Integer limit);

        /**
         * 获取热门电影（按评分和观看次数综合排序）
         */
        List<MovieInfo> listHot(Integer limit);

        /**
         * 根据分类查询电影（分页）
         */
        IPage<MovieInfo> queryMovieInfoByCategory(MovieQueryDTO dto);

        /**
         * 查询电影（不含分类筛选）
         */
        IPage<MovieInfo> queryMovieInfo(MovieQueryDTO dto);

        /**
         * 获取电影详情
         */
        MovieDetailDTO getMovieDetail(Long id);

}