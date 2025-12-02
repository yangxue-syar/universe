package com.universe.crawler.omdb;

import com.universe.entity.MovieInfo;
import com.universe.service.MovieInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 简单示例：如何用 OMDbMovieParser 获取并保存电影到数据库
 */
@Service
public class OMDbCrawlerExample {
    private final OMDbMovieParser parser;
    private final MovieInfoService movieInfoService;

    public OMDbCrawlerExample(OMDbMovieParser parser, MovieInfoService movieInfoService) {
        this.parser = parser;
        this.movieInfoService = movieInfoService;
    }

//    @Transactional
//    public void fetchAndSaveByImdbId(String imdbId) {
//        MovieInfo movie = parser.fetchByImdbId(imdbId);
//        if (movie == null) {
//            return;
//        }
//        // 去重：根据 source_url 或 imdbID（你可在 movie_info 中新增 imdb_id 字段）
//        if (movie.getSourceUrl() != null) {
//            // 简单示例：用 source_url 去重
//            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MovieInfo> qw =
//                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
//            qw.eq("source_url", movie.getSourceUrl());
//            MovieInfo exist = movieInfoService.getOne(qw);
//            if (exist != null) {
//                movie.setId(exist.getId());
//                movieInfoService.updateById(movie);
//                return;
//            }
//        }
//        movieInfoService.save(movie);
//    }
}
