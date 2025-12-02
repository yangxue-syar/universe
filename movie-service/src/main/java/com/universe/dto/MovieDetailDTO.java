package com.universe.dto;


import com.universe.entity.MovieInfo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
/**
 * MovieDetailDTO 类用于封装电影详细信息的数据传输对象
 * 该类作为电影信息的容器，在系统各层之间传递数据
 */
public class MovieDetailDTO {
    /**
     * 电影ID
     */
    private Long id;

    /**
     * 电影名称
     */
    private String title;

    /**
     * 原始名称
     */
    private String originalTitle;

    /**
     * 海报URL
     */
    private String poster;

    /**
     * 封面URL
     */
    private String cover;

    /**
     * 导演
     */
    private String director;

    /**
     * 演员列表
     */
    private String actors;

    /**
     * 剧情简介
     */
    private String description;

    /**
     * 上映日期
     */
    private Date releaseDate;

    /**
     * 片长(分钟)
     */
    private Integer duration;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 观看次数
     */
    private Long viewsCount;

    /**
     * 资源链接
     */
    private String sourceUrl;
}
