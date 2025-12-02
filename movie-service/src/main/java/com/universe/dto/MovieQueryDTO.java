package com.universe.dto;

import lombok.Data;

@Data
public class MovieQueryDTO {
    private String title;
    private Integer status;
    private Long categoryId;
    private Integer year;
    private Integer yearFrom;  // 新增：年份区间开始
    private Integer yearTo;    // 新增：年份区间结束
    private Double minRating;
    private String region;     // 新增：地区筛选
    private String language;   // 新增：语言筛选
    private String sortBy;
    private int page = 1;
    private int size = 10;
}
