package com.universe.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {

    @Bean
    @ConditionalOnMissingBean(name = "corsFilter")
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 【关键修改】不使用通配符，明确指定允许的源
        config.addAllowedOriginPattern("http://localhost:5173");
        config.addAllowedOriginPattern("http://localhost:*");
        config.addAllowedOriginPattern("http://192.168.0.*:*");

        // 允许携带凭证
        config.setAllowCredentials(true);

        // 允许所有请求方法
        config.addAllowedMethod("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 暴露的响应头（如果需要前端读取某些header）
        config.addExposedHeader("*");
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");

        // 预检请求缓存时间
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}