package com.universe.user;

import com.universe.common.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {UserServiceApplication.class, Token.TestConfig.class})
public class Token {

    @Autowired
    private JwtUtils jwtUtil;

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(1L, "test_user");
        System.out.println("✅ 生成的 token = " + token);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public JwtUtils jwtUtils() {
            return new JwtUtils();
        }
    }
}

