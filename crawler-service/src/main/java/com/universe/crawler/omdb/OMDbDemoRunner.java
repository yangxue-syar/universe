package com.universe.crawler.omdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 启动时的演示 Runner（可选）
 * - 读取配置 omdb.demo.run=true/false
 * - 读取 omdb.demo.imdb-id 指定演示的 imdbId
 *
 * 如果你不想在启动时执行，把此类删除或将 omdb.demo.run 设为 false。
 */
@Component
public class OMDbDemoRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(OMDbDemoRunner.class);

    private final Environment env;
    private final OMDbFetchAndSaveService fetchAndSaveService;

    public OMDbDemoRunner(Environment env, OMDbFetchAndSaveService fetchAndSaveService) {
        this.env = env;
        this.fetchAndSaveService = fetchAndSaveService;
    }

    @Override
    public void run(String... args) {
        boolean demoRun = Boolean.parseBoolean(env.getProperty("omdb.demo.run", "false"));
        if (!demoRun) {
            return;
        }
        String imdbId = env.getProperty("omdb.demo.imdb-id", "tt28682323");
        log.info("OMDb demo runner triggered for imdbId={}", imdbId);
        fetchAndSaveService.fetchAndSaveByImdbId(imdbId);
        log.info("OMDb demo runner finished for imdbId={}", imdbId);
    }
}
