package com.universe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.universe.dto.MovieDetailDTO;
import com.universe.dto.MovieQueryDTO;
import com.universe.entity.MovieInfo;
import com.universe.mapper.MovieInfoMapper;
import com.universe.service.MovieInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovieInfoServiceImpl extends ServiceImpl<MovieInfoMapper, MovieInfo>
        implements MovieInfoService {
    @Resource
    private MovieInfoMapper movieInfoMapper;

    @Override
    public IPage<MovieInfo> queryMovieInfo(MovieQueryDTO dto) {
        Page<MovieInfo> page = new Page<>(dto.getPage(), dto.getSize());

        LambdaQueryWrapper<MovieInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieInfo::getStatus, 1); // 只查询上架的电影

        // 添加筛选条件
        if (dto.getYear() != null) {
            LocalDate startDate = LocalDate.of(dto.getYear(), 1, 1);
            LocalDate endDate = LocalDate.of(dto.getYear(), 12, 31);
            wrapper.between(MovieInfo::getReleaseDate, startDate, endDate);
        }

        if (dto.getMinRating() != null) {
            wrapper.ge(MovieInfo::getRating, dto.getMinRating());
        }

        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            wrapper.like(MovieInfo::getTitle, dto.getTitle());
        }

        // 排序逻辑
        if ("rating".equals(dto.getSortBy())) {
            wrapper.orderByDesc(MovieInfo::getRating);
        } else if ("views".equals(dto.getSortBy())) {
            wrapper.orderByDesc(MovieInfo::getViewsCount);
        } else {
            // 默认按最新上映排序
            wrapper.orderByDesc(MovieInfo::getReleaseDate);
        }

        return this.page(page, wrapper);
    }

    @Override
    public IPage<MovieInfo> queryMovieInfoByCategory(MovieQueryDTO dto) {
        Page<MovieInfo> page = new Page<>(dto.getPage(), dto.getSize());
        return movieInfoMapper.selectPageByCategory(page,dto);
    }

    @Override
    public MovieDetailDTO getMovieDetail(Long id) {
        MovieInfo movie = this.getById(id);
        if (movie == null) {
            return null;
        }

        // 增加观看次数
        movie.setViewsCount(movie.getViewsCount() + 1);
        this.updateById(movie);

        // 转换为详情DTO
        MovieDetailDTO detail = new MovieDetailDTO();
        detail.setId(movie.getId());
        detail.setTitle(movie.getTitle());
        detail.setOriginalTitle(movie.getOriginalTitle());
        detail.setPoster(movie.getPoster());
        detail.setCover(movie.getCover());
        detail.setDirector(movie.getDirector());
        detail.setActors(movie.getActors());
        detail.setDescription(movie.getDescription());
        detail.setReleaseDate(movie.getReleaseDate());
        detail.setDuration(movie.getDuration());
        detail.setRating(movie.getRating());
        detail.setViewsCount(movie.getViewsCount());
        detail.setSourceUrl(movie.getSourceUrl());

        return detail;
    }

    @Override
    public List<MovieInfo> listHot(int limit) {
        LambdaQueryWrapper<MovieInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MovieInfo::getStatus, 1)
                .orderByDesc(MovieInfo::getRating)
                .orderByDesc(MovieInfo::getViewsCount)
                .last("LIMIT " + limit);

        return this.list(wrapper);
    }

    /**
     * 获取最新上映的电影（按上映日期降序）
     *
     * @param limit 返回数量
     * @return 最新电影列表
     */
    @Override
    public List<MovieInfo> getLatestMovies(int limit) {
        LambdaQueryWrapper<MovieInfo> wrapper = new LambdaQueryWrapper<>();

        // 构建查询条件
        wrapper.eq(MovieInfo::getStatus, 1)                    // 只查询上架的电影
                .isNotNull(MovieInfo::getReleaseDate)           // 必须有上映日期
                .isNotNull(MovieInfo::getCover)                 // 必须有封面图（轮播图需要）
                .orderByDesc(MovieInfo::getReleaseDate)         // 按上映日期降序排序
                .orderByDesc(MovieInfo::getRating)              // 相同日期按评分降序排序
                .last("LIMIT " + limit);                        // 限制返回数量

        // 执行查询
        List<MovieInfo> movies = this.list(wrapper);

        return movies;
    }
    @Override
    public List<MovieInfo> getLatestMovies(Integer limit) {
        return movieInfoMapper.selectList(
                new LambdaQueryWrapper<MovieInfo>()
                        .eq(MovieInfo::getStatus, 1) // 只查上架的
                        .orderByDesc(MovieInfo::getReleaseDate)
                        .last("LIMIT " + limit)
        );
    }

    @Override
    public List<MovieInfo> listHot(Integer limit) {
        // 综合评分和观看次数排序
        // 可以使用公式：热度 = rating * 0.5 + (views_count / 10000) * 0.5
        return movieInfoMapper.selectList(
                new LambdaQueryWrapper<MovieInfo>()
                        .eq(MovieInfo::getStatus, 1)
                        .orderByDesc(MovieInfo::getRating)
                        .orderByDesc(MovieInfo::getViewsCount)
                        .last("LIMIT " + limit)
        );
    }

}