package com.universe.crawler.omdb;

import com.universe.entity.MovieInfo;

import java.util.List;

/**
 * 包含映射后的 MovieInfo 与解析出的 genre 名称列表
 */
public class OMDbParseResult {
    private MovieInfo movie;
    private List<String> genres;
    private String sourceUrl;

    public OMDbParseResult() {}

    public OMDbParseResult(MovieInfo movie, List<String> genres, String sourceUrl) {
        this.movie = movie;
        this.genres = genres;
        this.sourceUrl = sourceUrl;
    }

    public MovieInfo getMovie() {
        return movie;
    }

    public void setMovie(MovieInfo movie) {
        this.movie = movie;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}