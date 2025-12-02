package com.universe.crawler;

import com.universe.entity.MovieInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 将 Jsoup Document 解析成 MovieInfo 和分类 ID（分类解析示例）
 * 注意：下面的 CSS selector 是示例，需根据目标站点调整
 */
public class MovieParser {

    public static class ParseResult {
        public MovieInfo movie;
        public List<Long> categoryIds = new ArrayList<>(); // 如果网站给出分类名称，需要做名称->id的映射
    }

    public ParseResult parseMovieDetail(Document doc, String sourceUrl) {
        ParseResult r = new ParseResult();
        MovieInfo m = new MovieInfo();

        // 示例 selector，请按实际页面调整
        Element titleEl = doc.selectFirst("h1.movie-title");
        m.setTitle(titleEl != null ? titleEl.text().trim() : "");

        Element originalTitleEl = doc.selectFirst(".original-title");
        m.setOriginalTitle(originalTitleEl != null ? originalTitleEl.text().trim() : null);

        Element posterEl = doc.selectFirst(".poster img");
        m.setPoster(posterEl != null ? posterEl.absUrl("src") : null);

        Element coverEl = doc.selectFirst(".cover img");
        m.setCover(coverEl != null ? coverEl.absUrl("src") : null);

        Element directorEl = doc.selectFirst(".director");
        m.setDirector(directorEl != null ? directorEl.text().trim() : null);

        // actors 列表
        Elements actorEls = doc.select(".actors li");
        if (actorEls != null && actorEls.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Element a : actorEls) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(a.text().trim());
            }
            m.setActors(sb.toString());
        }

        Element descEl = doc.selectFirst(".description");
        m.setDescription(descEl != null ? descEl.text().trim() : null);

        Element releaseEl = doc.selectFirst(".release-date");
        if (releaseEl != null) {
            try {
                String text = releaseEl.text().trim();
                // 尝试解析 yyyy-MM-dd
                DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate ld = LocalDate.parse(text, f);
                m.setReleaseDate(java.sql.Date.valueOf(ld));
            } catch (Exception ignored) {}
        }

        Element durationEl = doc.selectFirst(".duration");
        if (durationEl != null) {
            try {
                String t = durationEl.text().replaceAll("[^0-9]", "");
                if (!t.isEmpty()) m.setDuration(Integer.parseInt(t));
            } catch (Exception ignored) {}
        }

        Element ratingEl = doc.selectFirst(".rating .score");
        if (ratingEl != null) {
            try {
                m.setRating(new java.math.BigDecimal(ratingEl.text().trim()));
            } catch (Exception ignored) {}
        }

        m.setSourceUrl(sourceUrl);
        m.setStatus((1));

        // 分类示例：如果页面显示分类名称，需要由分类名映射到分类ID（lookup）
        Elements catEls = doc.select(".categories a");
        for (Element c : catEls) {
            String catName = c.text().trim();
            // TODO: 在保存阶段做 catName -> id 的映射，或者在这里调用 CategoryService 查找 id
            // 此处仅演示将分类名以占位符方式记录（实际应转换）
        }

        r.movie = m;
        return r;
    }
}