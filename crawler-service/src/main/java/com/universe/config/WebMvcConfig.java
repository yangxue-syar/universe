package com.universe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/crawler/**") // 仅对爬虫接口生效
                .allowedOrigins("*") // 生产环境替换为具体前端域名
                .allowedMethods("POST") // 只允许POST方法
                .allowedHeaders("*") // 允许所有请求头
                .maxAge(3600); // 预检请求缓存时间（秒）
    }
}