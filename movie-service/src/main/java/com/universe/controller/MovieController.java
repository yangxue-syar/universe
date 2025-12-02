package com.universe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.universe.common.Result;
import com.universe.dto.MovieDetailDTO;
import com.universe.dto.MovieQueryDTO;
import com.universe.entity.MovieInfo;
import com.universe.service.MovieInfoService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Resource
    private MovieInfoService movieInfoService;

    @GetMapping("/test")
    public String test() {
        logger.info(" /movie/test 端点被调用");
        return " movie-service is running!";
    }

    /**
     * 分页查询电影信息（支持多条件筛选）
     * @param dto 查询条件对象，包含分页参数、分类ID、年份、评分等筛选条件
     * @return 返回分页电影信息列表
     */
    @GetMapping("/page")
    public Result<IPage<MovieInfo>> page(MovieQueryDTO dto) {
        logger.info("分页查询电影，参数: {}", dto);

        try {
            IPage<MovieInfo> pageData;

            // 如果有分类ID，使用带分类筛选的查询
            if (dto.getCategoryId() != null) {
                pageData = movieInfoService.queryMovieInfoByCategory(dto);
            } else {
                // 否则使用普通查询
                pageData = movieInfoService.queryMovieInfo(dto);
            }

            logger.info("成功查询电影，总数: {}, 当前页: {}",
                    pageData.getTotal(), pageData.getCurrent());

            return Result.success(pageData);

        } catch (Exception e) {
            logger.error("分页查询电影失败", e);
            return Result.error(500, "查询电影列表失败: " + e.getMessage());
        }
    }


    /**
     * 获取推荐电影列表（热门电影）
     * @return 返回热门电影列表
     */
    @GetMapping("/recommend")
    public Result<List<MovieInfo>> recommend(
            @RequestParam(defaultValue = "10") Integer limit) {

        logger.info("获取推荐电影，数量: {}", limit);

        try {
            List<MovieInfo> movies = movieInfoService.listHot(limit);
            logger.info(" 成功获取 {} 条推荐电影", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error(" 获取推荐电影失败", e);
            return Result.error(500, "获取推荐电影失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取轮播图电影列表（按上映日期降序）
     * ⚠️ 注意：此接口必须放在 /{id} 之前，避免路径冲突
     *
     * @param limit 返回数量，默认8条
     * @return 轮播图电影列表
     */
    @GetMapping("/carousel/latest")
    public Result<List<MovieInfo>> getCarouselMovies(
            @RequestParam(defaultValue = "8") Integer limit) {

        logger.info("获取最新轮播图电影，数量: {}", limit);

        try {
            List<MovieInfo> movies = movieInfoService.getLatestMovies(limit);
            logger.info("成功获取 {} 条最新轮播图电影", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("获取最新轮播图电影失败", e);
            return Result.error(500, "获取最新轮播图电影失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取热门轮播图电影（按评分和观看次数综合排序）
     *
     * @param limit 返回数量，默认8条
     * @return 热门轮播图电影列表
     */
    @GetMapping("/carousel/hot")
    public Result<List<MovieInfo>> getHotCarouselMovies(
            @RequestParam(defaultValue = "8") Integer limit) {

        logger.info("获取热门轮播图电影，数量: {}", limit);

        try {
            List<MovieInfo> movies = movieInfoService.listHot(limit);
            logger.info("成功获取 {} 条热门轮播图电影", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("获取热门轮播图电影失败", e);
            return Result.error(500, "获取热门轮播图电影失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 根据主分类ID获取内容列表（支持电影、电视剧、综艺、动漫）
     *
     * @param typeId 主分类ID (1=电影, 2=电视剧, 3=综艺, 4=动漫)
     * @param limit 返回数量，默认10条
     * @param sortBy 排序方式 (latest=最新, hot=最热)
     * @return 内容列表
     */
    @GetMapping("/list/by-type")
    public Result<List<MovieInfo>> getListByType(
            @RequestParam Long typeId,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "latest") String sortBy) {

        logger.info("🎬 获取类型内容列表，类型ID: {}, 数量: {}, 排序: {}", typeId, limit, sortBy);

        try {
            MovieQueryDTO dto = new MovieQueryDTO();
            dto.setCategoryId(typeId);
            dto.setPage(1);
            dto.setSize(limit);
            dto.setSortBy(sortBy);
            dto.setStatus(1); // 只查询上架的内容

            IPage<MovieInfo> pageData = movieInfoService.queryMovieInfoByCategory(dto);
            List<MovieInfo> movies = pageData.getRecords();

            logger.info("✅ 成功获取 {} 条类型内容", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("❌ 获取类型内容失败", e);
            return Result.error(500, "获取内容列表失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取正在热播内容（综合所有类型）
     *
     * @param limit 返回数量，默认10条
     * @return 热播内容列表
     */
    @GetMapping("/list/trending")
    public Result<List<MovieInfo>> getTrending(
            @RequestParam(defaultValue = "10") Integer limit) {

        logger.info("🔥 获取正在热播内容，数量: {}", limit);

        try {
            List<MovieInfo> movies = movieInfoService.listHot(limit);
            logger.info("✅ 成功获取 {} 条热播内容", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("❌ 获取热播内容失败", e);
            return Result.error(500, "获取热播内容失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取最新电影
     *
     * @param limit 返回数量，默认10条
     * @return 最新电影列表
     */
    @GetMapping("/list/latest-movies")
    public Result<List<MovieInfo>> getLatestMovies(
            @RequestParam(defaultValue = "10") Integer limit) {

        logger.info("🎬 获取最新电影，数量: {}", limit);

        try {
            MovieQueryDTO dto = new MovieQueryDTO();
            dto.setCategoryId(1L); // 电影类型ID = 1
            dto.setPage(1);
            dto.setSize(limit);
            dto.setSortBy("latest");
            dto.setStatus(1);

            IPage<MovieInfo> pageData = movieInfoService.queryMovieInfoByCategory(dto);
            List<MovieInfo> movies = pageData.getRecords();

            logger.info("✅ 成功获取 {} 条最新电影", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("❌ 获取最新电影失败", e);
            return Result.error(500, "获取最新电影失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取最新电视剧
     *
     * @param limit 返回数量，默认10条
     * @return 最新电视剧列表
     */
    @GetMapping("/list/latest-tv")
    public Result<List<MovieInfo>> getLatestTv(
            @RequestParam(defaultValue = "10") Integer limit) {

        logger.info("📺 获取最新电视剧，数量: {}", limit);

        try {
            MovieQueryDTO dto = new MovieQueryDTO();
            dto.setCategoryId(2L); // 电视剧类型ID = 2
            dto.setPage(1);
            dto.setSize(limit);
            dto.setSortBy("latest");
            dto.setStatus(1);

            IPage<MovieInfo> pageData = movieInfoService.queryMovieInfoByCategory(dto);
            List<MovieInfo> movies = pageData.getRecords();

            logger.info("✅ 成功获取 {} 条最新电视剧", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("❌ 获取最新电视剧失败", e);
            return Result.error(500, "获取最新电视剧失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取最新综艺
     *
     * @param limit 返回数量，默认10条
     * @return 最新综艺列表
     */
    @GetMapping("/list/latest-variety")
    public Result<List<MovieInfo>> getLatestVariety(
            @RequestParam(defaultValue = "10") Integer limit) {

        logger.info("🎤 获取最新综艺，数量: {}", limit);

        try {
            MovieQueryDTO dto = new MovieQueryDTO();
            dto.setCategoryId(3L); // 综艺类型ID = 3
            dto.setPage(1);
            dto.setSize(limit);
            dto.setSortBy("latest");
            dto.setStatus(1);

            IPage<MovieInfo> pageData = movieInfoService.queryMovieInfoByCategory(dto);
            List<MovieInfo> movies = pageData.getRecords();

            logger.info("✅ 成功获取 {} 条最新综艺", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("❌ 获取最新综艺失败", e);
            return Result.error(500, "获取最新综艺失败: " + e.getMessage());
        }
    }

    /**
     * 🆕 获取最新动漫
     *
     * @param limit 返回数量，默认10条
     * @return 最新动漫列表
     */
    @GetMapping("/list/latest-animation")
    public Result<List<MovieInfo>> getLatestAnimation(
            @RequestParam(defaultValue = "10") Integer limit) {

        logger.info("🎨 获取最新动漫，数量: {}", limit);

        try {
            MovieQueryDTO dto = new MovieQueryDTO();
            dto.setCategoryId(4L); // 动漫类型ID = 4
            dto.setPage(1);
            dto.setSize(limit);
            dto.setSortBy("latest");
            dto.setStatus(1);

            IPage<MovieInfo> pageData = movieInfoService.queryMovieInfoByCategory(dto);
            List<MovieInfo> movies = pageData.getRecords();

            logger.info("✅ 成功获取 {} 条最新动漫", movies.size());
            return Result.success(movies);

        } catch (Exception e) {
            logger.error("❌ 获取最新动漫失败", e);
            return Result.error(500, "获取最新动漫失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取电影详细信息
     * ⚠️ 注意：此接口必须放在最后，因为使用了路径变量 {id}
     *
     * @param id 电影ID
     * @return 返回电影详细信息对象
     */
    @GetMapping("/detail/{id}")
    public Result<MovieDetailDTO> getMovieDetail(@PathVariable Long id) {
        logger.info("🎬 获取电影详情，ID: {}", id);

        try {
            MovieDetailDTO movieDetail = movieInfoService.getMovieDetail(id);

            if (movieDetail == null) {
                logger.warn("电影不存在，ID: {}", id);
                return Result.error(404, "电影不存在");
            }

            logger.info("成功获取电影详情: {}", movieDetail.getTitle());
            return Result.success(movieDetail);

        } catch (Exception e) {
            logger.error("获取电影详情失败，ID: {}", id, e);
            return Result.error(500, "获取电影详情失败: " + e.getMessage());
        }
    }
}