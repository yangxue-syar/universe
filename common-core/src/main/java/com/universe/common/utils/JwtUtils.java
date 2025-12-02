package com.universe.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtils {

    private static final String SECRET = "user_service_secret_key";
    private static final long EXPIRE = 1000 * 60 * 60 * 24;

    public static String generateToken(Long id, String username) {
        return JWT.create()
                .withClaim("id", id)
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE))
                .sign(Algorithm.HMAC256(SECRET));
    }
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
}
