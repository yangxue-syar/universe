package com.universe.common.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {
    @Bean
    public static String encrypt(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt());
    }

    public static boolean matches(String raw, String hashed) {
        if (raw == null || hashed == null) return false;
        return BCrypt.checkpw(raw, hashed);
    }
}
