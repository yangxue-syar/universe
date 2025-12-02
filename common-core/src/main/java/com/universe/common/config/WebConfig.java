package com.universe.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 标识为配置类
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置跨域资源共享
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 对所有接口生效
//                // 允许的前端域名/端口（根据实际前端地址修改）
//                .allowedOrigins("*")
//                // 允许的请求方法（GET/POST/PUT/DELETE等）
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                // 允许的请求头（*表示所有）
//                .allowedHeaders("*")
//                // 是否允许携带凭证（如Cookie，需与前端配合）
//                .allowCredentials(true)
//                // 预检请求的缓存时间（单位：秒，减少重复预检请求）
//                .maxAge(3600);
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加对favicon.ico的处理
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");

        // 确保默认的静态资源处理仍然有效
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);

    }
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 设置路径匹配，确保控制器优先于静态资源处理
        configurer.setUseTrailingSlashMatch(false);
    }
}
