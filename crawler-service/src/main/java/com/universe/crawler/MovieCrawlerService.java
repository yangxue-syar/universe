package com.universe.crawler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.RateLimiter;
import com.universe.entity.MovieCategoryRelation;
import com.universe.entity.MovieInfo;
import com.universe.service.MovieCategoryRelationService;
import com.universe.service.MovieInfoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

/**
 * 简单的爬虫服务：fetch -> parse -> save
 * 注意：大量业务代码（如分类名->id 映射、图片下载）应按需补充
 */
@Service
public class MovieCrawlerService {
    private static final Logger log = LoggerFactory.getLogger(MovieCrawlerService.class);

    private final MovieInfoService movieInfoService;
    private final MovieCategoryRelationService movieCategoryRelationService;
    private final MovieParser parser = new MovieParser();

    @Value("${crawler.user-agent}")
    private String userAgent;

    @Value("${crawler.timeout}")
    private int timeout;

    @Value("${crawler.max-threads}")
    private int maxThreads;

    @Value("${crawler.rate-per-second}")
    private double ratePerSecond;

    private ExecutorService pool;
    private RateLimiter rateLimiter;

    public MovieCrawlerService(MovieInfoService movieInfoService,
                               MovieCategoryRelationService movieCategoryRelationService) {
        this.movieInfoService = movieInfoService;
        this.movieCategoryRelationService = movieCategoryRelationService;
    }

    @PostConstruct
    public void init() {
        pool = Executors.newFixedThreadPool(Math.max(1, maxThreads));
        rateLimiter = RateLimiter.create(ratePerSecond);
    }

    /**
     * 抓取多个详情页的入口
     */
    public void crawlList(List<String> detailUrls) {
        CountDownLatch latch = new CountDownLatch(detailUrls.size());
        for (String url : detailUrls) {
            pool.submit(() -> {
                try {
                    rateLimiter.acquire(); // 限速
                    crawlDetail(url);
                } catch (Exception e) {
                    log.error("crawl failed for url: {}", url, e);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 单页抓取：fetch -> parse -> save
     */
    public void crawlDetail(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(timeout)
                    .referrer("https://www.google.com")
                    .get();
            MovieParser.ParseResult result = parser.parseMovieDetail(doc, url);
            if (result == null || result.movie == null) {
                log.warn("parse returned null for url={}", url);
                return;
            }
            saveMovie(result.movie, result.categoryIds);
        } catch (Exception e) {
            log.error("fetch or parse failed for url={}", url, e);
            // 可增加重试逻辑（exponential backoff）
        }
    }

    /**
     * 保存电影和分类关联；去重检查 source_url
     */
    @Transactional
    public void saveMovie(MovieInfo movie, List<Long> categoryIds) {
        // 去重：根据 source_url 查找
        if (movie.getSourceUrl() != null) {
            MovieInfo existing = movieInfoService.getOne(new QueryWrapper<MovieInfo>().eq("source_url", movie.getSourceUrl()));
            if (existing != null) {
                // 更新逻辑：可根据需要只更新部分字段或跳过
                movie.setId(existing.getId());
                movieInfoService.updateById(movie);
                // 更新分类关联
                if (categoryIds != null && !categoryIds.isEmpty()) {
                    movieCategoryRelationService.bind(existing.getId(), categoryIds);
                }
                log.info("updated existing movie id={} title={}", existing.getId(), movie.getTitle());
                return;
            }
        }

        // 新增
        movieInfoService.save(movie);
        Long movieId = movie.getId();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            movieCategoryRelationService.bind(movieId, categoryIds);
        }
        log.info("saved movie id={} title={}", movieId, movie.getTitle());
    }
}