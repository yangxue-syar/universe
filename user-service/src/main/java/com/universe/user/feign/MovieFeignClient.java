package com.universe.user.feign;

import com.universe.common.Result;
import com.universe.dto.MovieDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 🎬 电影服务远程调用接口
 *
 * ✅ 通过 OpenFeign + Nacos 实现服务间调用
 *
 * 📌 配置说明：
 * - name = "movie-service" → Nacos 中注册的服务名称（必须与 movie-service 的 spring.application.name 一致）
 * - path = "/movie" → 接口的统一前缀
 *
 * 🔥 调用流程：
 * 1. user-service 调用 movieFeignClient.getMovieDetail(101)
 * 2. Feign 通过 Nacos 发现 movie-service 的实例
 * 3. 发起 HTTP 请求：GET http://movie-service/movie/detail/101
 * 4. movie-service 处理请求并返回结果
 *
 * ⚠️ 注意事项：
 * - movie-service 必须在 Nacos 中注册（application.yml 中配置）
 * - 服务名称必须完全匹配
 * - 如果不使用 Nacos，可以用 url 属性直接指定地址：
 *   @FeignClient(name = "movie-service", url = "http://localhost:8081")
 */
@FeignClient(
        name = "movie-service",    // ✅ Nacos 中注册的服务名称
        path = "/movie"            // ✅ movie-service 的接口前缀
)
public interface MovieFeignClient {

    /**
     * 获取电影详细信息
     *
     * 🎯 调用 movie-service 的 /movie/detail/{id} 接口
     *
     * @param id 电影ID
     * @return 电影详细信息（包含标题、海报、评分等）
     */
    @GetMapping("/detail/{id}")
    Result<MovieDetailDTO> getMovieDetail(@PathVariable("id") Long id);
}