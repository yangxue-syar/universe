package com.universe.user.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.common.Result;
import com.universe.common.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    // 白名单路径 - 这些路径不需要JWT验证
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/favicon.ico",
            "/actuator"
    );

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        log.info("========================================");
        log.info("JwtFilter 拦截请求");
        log.info("URI: {}", requestURI);
        log.info("Method: {}", method);
        log.info("========================================");

        // 检查是否在白名单中
        if (isExcludedPath(requestURI)) {
            log.info("路径在白名单中，直接放行: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        // OPTIONS 请求直接放行（CORS 预检）
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.info("OPTIONS 请求，直接放行");
            chain.doFilter(request, response);
            return;
        }

        // 获取 token
        String token = httpRequest.getHeader("Authorization");
        log.info("Authorization Header: {}", token);

        // 如果没有token，返回401
        if (token == null || token.isEmpty()) {
            log.warn("缺少 Token，拒绝访问: {}", requestURI);
            handleAuthenticationFailure(httpResponse, "Missing authentication token");
            return;
        }

        try {
            // 移除 "Bearer " 前缀并验证token
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            DecodedJWT jwt = JwtUtils.verifyToken(actualToken);

            // 将JWT信息设置到request属性中，供后续使用
            request.setAttribute("jwt.payload", jwt);
            request.setAttribute("userId", jwt.getClaim("id").asLong());
            request.setAttribute("username", jwt.getClaim("username").asString());

            log.info("Token 验证成功，用户: {}", jwt.getClaim("username").asString());

            // 验证成功，继续过滤链
            chain.doFilter(request, response);

        } catch (Exception ex) {
            // token验证失败
            log.error("Token 验证失败: {}", ex.getMessage());
            handleAuthenticationFailure(httpResponse, "Invalid or expired token");
        }
    }

    /**
     * 检查路径是否在排除列表中
     */
    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream().anyMatch(requestURI::startsWith);
    }

    /**
     * 处理认证失败
     */
    private void handleAuthenticationFailure(HttpServletResponse response, String message) throws IOException {
        log.error("=== 认证失败 ===");
        log.error("错误信息: {}", message);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Result<?> result = Result.error(401, message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
    }
}