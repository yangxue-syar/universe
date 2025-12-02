package com.universe.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务举例：每 xx 分钟抓取列表页并提取详情链接
 */
@Component
public class CrawlerScheduler {

    private final MovieCrawlerService movieCrawlerService;

    public CrawlerScheduler(MovieCrawlerService movieCrawlerService) {
        this.movieCrawlerService = movieCrawlerService;
    }

    // cron 或 fixedDelay，根据需求设置
    @Scheduled(fixedDelayString = "${crawler.schedule-delay-ms:300000}")
    public void scheduledCrawl() {
        // 这个例子抓取一个示例的列表页，实际需要按站点调整
        List<String> detailUrls = new ArrayList<>();
        try {
            String listUrl = " http://192.168.0.80:9103/movies/page/1"; // 修改为真实列表页
            Document doc = Jsoup.connect(listUrl).userAgent("Mozilla/5.0").get();
            Elements links = doc.select(".movie-list .movie-item a.detail-link");
            for (Element a : links) {
                String href = a.absUrl("href");
                if (href != null && !href.isEmpty()) {
                    detailUrls.add(href);
                }
            }
        } catch (Exception e) {
            // log
        }

        if (!detailUrls.isEmpty()) {
            movieCrawlerService.crawlList(detailUrls);
        }
    }
}