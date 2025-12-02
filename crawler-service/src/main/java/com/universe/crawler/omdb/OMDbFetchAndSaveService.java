package com.universe.crawler.omdb;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.universe.entity.MovieCategory;
import com.universe.entity.MovieInfo;
import com.universe.service.MovieCategoryRelationService;
import com.universe.service.MovieCategoryService;
import com.universe.service.MovieInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 改进后的服务：从 OMDb 拉取，保存 MovieInfo，并把 Genre -> movie_category（自动创建）并写入 movie_category_relation
 */
@Service
public class OMDbFetchAndSaveService {
    private static final Logger log = LoggerFactory.getLogger(OMDbFetchAndSaveService.class);

    private final OMDbMovieParser parser;
    private final MovieInfoService movieInfoService;
    private final MovieCategoryService movieCategoryService;
    private final MovieCategoryRelationService movieCategoryRelationService;

    public OMDbFetchAndSaveService(OMDbMovieParser parser,
                                   MovieInfoService movieInfoService,
                                   MovieCategoryService movieCategoryService,
                                   MovieCategoryRelationService movieCategoryRelationService) {
        this.parser = parser;
        this.movieInfoService = movieInfoService;
        this.movieCategoryService = movieCategoryService;
        this.movieCategoryRelationService = movieCategoryRelationService;
    }

    /**
     * 拉取并保存（并将 genre 名称映射/创建为 movie_category，并 bind 关联）
     */
    @Transactional
    public void fetchAndSaveByImdbId(String imdbId) {
        if (imdbId == null || imdbId.isBlank()) {
            log.warn("imdbId is blank, skip.");
            return;
        }

        OMDbParseResult result = parser.fetchByImdbIdWithGenres(imdbId);
        if (result == null || result.getMovie() == null) {
            log.warn("No movie returned from OMDb for imdbId={}", imdbId);
            return;
        }

        MovieInfo movie = result.getMovie();
        String sourceUrl = result.getSourceUrl();

        try {
            // 去重/更新（根据 source_url）
            if (sourceUrl != null) {
                QueryWrapper<MovieInfo> qw = new QueryWrapper<>();
                qw.eq("source_url", sourceUrl);
                MovieInfo exist = movieInfoService.getOne(qw);
                if (exist != null) {
                    movie.setId(exist.getId());
                    movieInfoService.updateById(movie);
                    log.info("Updated existing movie id={} title={} imdbId={}", exist.getId(), movie.getTitle(), imdbId);
                } else {
                    movieInfoService.save(movie);
                    log.info("Saved new movie id={} title={} imdbId={}", movie.getId(), movie.getTitle(), imdbId);
                }
            } else {
                movieInfoService.save(movie);
                log.info("Saved new movie id={} title={} imdbId={}", movie.getId(), movie.getTitle(), imdbId);
            }

            Long movieId = movie.getId();
            if (movieId == null) {
                log.warn("movieId is null after save for imdbId={}", imdbId);
                return;
            }

            // 处理 genres -> movie_category，自动创建未命中的分类
            List<String> genres = result.getGenres();
            if (genres != null && !genres.isEmpty()) {
                List<Long> categoryIds = new ArrayList<>();
                for (String g : genres) {
                    if (g == null || g.isBlank()) continue;
                    String name = g.trim();
                    QueryWrapper<MovieCategory> cw = new QueryWrapper<>();
                    cw.eq("category_name", name);
                    MovieCategory cat = movieCategoryService.getOne(cw);
                    if (cat == null) {
                        // 未命中则创建
                        MovieCategory nc = new MovieCategory();
                        nc.setCategoryName(name);
                        // 保持 parent_id 和 sort 为默认 null/0，如需不同可扩展
                        movieCategoryService.save(nc);
                        cat = nc;
                        log.info("Created new category name={} id={}", name, nc.getId());
                    }
                    if (cat != null && cat.getId() != null) {
                        categoryIds.add(cat.getId());
                    }
                }
                if (!categoryIds.isEmpty()) {
                    // bind 会先删除该 movie 的旧关系再批量保存新关系
                    movieCategoryRelationService.bind(movieId, categoryIds);
                    log.info("Bound movieId={} to categories {}", movieId, categoryIds);
                }
            }

        } catch (Exception e) {
            log.error("Error saving movie or binding categories for imdbId={} sourceUrl={}", imdbId, sourceUrl, e);
            // 事务会回滚，如果需要额外补偿逻辑可在这里实现
            throw e;
        }
    }
}