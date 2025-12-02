package com.universe.crawler.omdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.entity.MovieInfo;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 本地 Demo：读取 classpath 下的 tt28682323.json（示例 OMDb 返回），
 * 将 JSON 映射为 MovieInfo 并打印出来。
 *
 * 使用方法：
 * 1. 把 tt28682323.json 放到 resources 同包路径（/com/universe/crawler/omdb/tt28682323.json）
 * 2. 运行本类的 main 方法（IDE 或 mvn exec:java）
 *
 * 说明：映射逻辑与之前提供的 OMDbMovieParser 保持一致（封装了常见格式解析和容错）。
 */
public class OMDbLiveDemo {

    public static MovieInfo mapJsonToMovieInfo(String json, String sourceUrl) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OMDbResponse r = objectMapper.readValue(json, OMDbResponse.class);
        if (r == null || !"True".equalsIgnoreCase(r.response)) {
            System.out.println("OMDb response indicates failure or null. error=" + (r != null ? r.error : "null"));
            return null;
        }

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

        // parse released date like "15 Oct 2024"
        if (r.released != null && !"N/A".equalsIgnoreCase(r.released)) {
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
                LocalDate ld = LocalDate.parse(r.released, f);
                m.setReleaseDate(java.sql.Date.valueOf(ld));
            } catch (Exception ex) {
                // fallback to year only
                try {
                    if (r.year != null && r.year.matches("\\d{4}")) {
                        m.setReleaseDate(java.sql.Date.valueOf(LocalDate.of(Integer.parseInt(r.year), 1, 1)));
                    }
                } catch (Exception ignore) {}
            }
        }

        // runtime like "125 min"
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
            } catch (Exception ignore) {}
        }

        return m;
    }

    private static String nonEmpty(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    public static void main(String[] args) throws Exception {
        // 从 classpath 读取示例 JSON（也可以直接把 JSON 字符串写在这里）
        String resourcePath = "/com/universe/crawler/omdb/tt28682323.json";
        try (InputStream in = OMDbLiveDemo.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                System.err.println("Cannot find resource: " + resourcePath);
                return;
            }
            byte[] data = in.readAllBytes();
            String json = new String(data, java.nio.charset.StandardCharsets.UTF_8);
            String sourceUrl = "http://www.omdbapi.com/?apikey=YOUR_KEY&i=tt28682323";

            MovieInfo movie = mapJsonToMovieInfo(json, sourceUrl);
            if (movie == null) {
                System.out.println("Mapping returned null.");
                return;
            }

            // 打印映射结果，字段按你 movie_info 表常用字段展示
            System.out.println("Mapped MovieInfo:");
            System.out.println(" title: " + movie.getTitle());
            System.out.println(" originalTitle: " + movie.getOriginalTitle());
            System.out.println(" director: " + movie.getDirector());
            System.out.println(" actors: " + movie.getActors());
            System.out.println(" description: " + movie.getDescription());
            System.out.println(" poster: " + movie.getPoster());
            System.out.println(" releaseDate: " + movie.getReleaseDate());
            System.out.println(" duration (min): " + movie.getDuration());
            System.out.println(" rating: " + movie.getRating());
            System.out.println(" sourceUrl: " + movie.getSourceUrl());

            // 下一步：把 movie 交给 movieInfoService 保存到数据库，例如：
            // movieInfoService.save(movie);
            // 如果需要先查重可用 movie.getSourceUrl() 去查
        }
    }
}
