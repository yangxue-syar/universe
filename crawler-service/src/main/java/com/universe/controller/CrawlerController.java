package com.universe.controller;

import com.universe.crawler.MovieCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/crawler")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST}, allowedHeaders = "*")
public class CrawlerController {
    @Resource
    private MovieCrawlerService movieCrawlerService;


    /**
     * 抓取多个详情页的入口
     * @param detailUrls 详情页的URL列表
     */
    @PostMapping("/crawlList")
    public void crawlList(@RequestBody List<String> detailUrls) {
        movieCrawlerService.crawlList(detailUrls);
    }

    /**
     * 抓取单个详情页
     * @param url 详情页的URL
     */
    @PostMapping("/crawlDetail")
    public void crawlDetail(@RequestParam String url) {
        movieCrawlerService.crawlDetail(url);
    }
}
