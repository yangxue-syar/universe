package com.universe.user.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户收藏电影信息DTO
 * 包含收藏记录 + 电影详细信息
 */
@Data
public class UserFavoriteDTO {

    // ========== 收藏记录信息 ==========
    /** 收藏记录ID */
    private Long favoriteId;

    /** 用户ID */
    private Long userId;

    /** 收藏时间 */
    private Date createTime;

    // ========== 电影详细信息（从 movie-service 获取）==========
    /** 电影ID */
    private Long movieId;

    /** 电影名称 */
    private String title;

    /** 原始名称 */
    private String originalTitle;

    /** 海报URL */
    private String poster;

    /** 封面URL */
    private String cover;

    /** 导演 */
    private String director;

    /** 演员列表 */
    private String actors;

    /** 剧情简介 */
    private String description;

    /** 上映日期 */
    private Date releaseDate;

    /** 片长(分钟) */
    private Integer duration;

    /** 评分 */
    private BigDecimal rating;

    /** 观看次数 */
    private Long viewsCount;
}