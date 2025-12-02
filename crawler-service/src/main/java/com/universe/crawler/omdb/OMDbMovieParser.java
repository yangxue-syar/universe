package com.universe.crawler.omdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.entity.MovieInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * 负责调用 OMDb API，将返回 JSON 映射为 MovieInfo，并提取 genre 名称列表
 */
@Component
public class OMDbMovieParser {
    private static final Logger log = LoggerFactory.getLogger(OMDbMovieParser.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.base:http://www.omdbapi.com/}")
    private String baseUrl;

    public OMDbMovieParser(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 根据 imdbId 拉取 OMDb，并返回 OMDbParseResult（包含 MovieInfo 与 genres 列表）
     */
    public OMDbParseResult fetchByImdbIdWithGenres(String imdbId) {
        if (imdbId == null || imdbId.isBlank()) return null;
        String url = String.format("%s?apikey=%s&i=%s&plot=full", baseUrl, apiKey, imdbId);
        try {
            String json = restTemplate.getForObject(url, String.class);
            if (json == null) {
                log.warn("OMDb returned null body for url={}", url);
                return null;
            }
            OMDbResponse resp = objectMapper.readValue(json, OMDbResponse.class);
            if (resp == null || !"True".equalsIgnoreCase(resp.response)) {
                log.warn("OMDb response failed for url={}, error={}", url, resp != null ? resp.error : "null");
                return null;
            }

            MovieInfo movie = mapToMovieInfo(resp, url);
            List<String> genres = parseGenres(resp.genre);
            return new OMDbParseResult(movie, genres, url);
        } catch (RestClientException re) {
            log.error("HTTP error when calling OMDb: {}", re.getMessage(), re);
            return null;
        } catch (Exception e) {
            log.error("Failed to parse OMDb JSON or map to MovieInfo: {}", e.getMessage(), e);
            return null;
        }
    }

    private MovieInfo mapToMovieInfo(OMDbResponse r, String sourceUrl) {
        MovieInfo m = new MovieInfo();
        m.setTitle(nonEmpty(r.title));
        m.setOriginalTitle(nonEmpty(r.title));
        m.setPoster(nonEmpty(r.poster));
        m.setCover(nonEmpty(r.poster));
        m.setDirector(nonEmpty(r.director));
        m.setActors(nonEmpty(r.actors));
        m.setDescription(nonEmpty(r.plot));
        m.setSourceUrl(nonEmpty(sourceUrl));
        m.setStatus(1);

        // release date 解析 e.g. "15 Oct 2024"
        if (r.released != null && !"N/A".equalsIgnoreCase(r.released)) {
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
                LocalDate ld = LocalDate.parse(r.released, f);
                m.setReleaseDate(java.sql.Date.valueOf(ld));
            } catch (DateTimeParseException ex) {
                try {
                    if (r.year != null && r.year.matches("\\d{4}")) {
                        m.setReleaseDate(java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(r.year), 1, 1)));
                    }
                } catch (Exception ignore) {}
            }
        }

        // runtime 解析 "125 min"
        if (r.runtime != null && r.runtime.matches(".*\\d+.*")) {
            try {
                String n = r.runtime.replaceAll("[^0-9]", "");
                if (!n.isEmpty()) m.setDuration(Integer.parseInt(n));
            } catch (Exception ignore) {}
        }

        // imdbRating -> decimal
        if (r.imdbRating != null && !"N/A".equalsIgnoreCase(r.imdbRating)) {
            try {
                m.setRating(new BigDecimal(r.imdbRating));
            } catch (NumberFormatException ignore) {}
        }

        return m;
    }

    private static List<String> parseGenres(String genreRaw) {
        if (genreRaw == null) return Collections.emptyList();
        genreRaw = genreRaw.trim();
        if (genreRaw.isEmpty() || "N/A".equalsIgnoreCase(genreRaw)) return Collections.emptyList();
        String[] parts = genreRaw.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) list.add(t);
        }
        return list;
    }

    private String nonEmpty(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}