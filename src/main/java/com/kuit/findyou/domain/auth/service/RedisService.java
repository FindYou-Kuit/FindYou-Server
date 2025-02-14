package com.kuit.findyou.domain.auth.service;

import com.kuit.findyou.global.common.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "RefreshToken:";

    public void saveRefreshToken(String refreshToken) {
        redisTemplate.opsForValue().set(createKey(refreshToken), refreshToken, jwtUtil.getRefresh_token_expiration(), TimeUnit.MILLISECONDS);
    }

    public Boolean isRefreshTokenExist(String refreshToken) {
        return redisTemplate.hasKey(createKey(refreshToken)) != null;
    }

    public String getRefreshToken(String refreshToken) {
        return (String) redisTemplate.opsForValue().get(createKey(refreshToken));
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(createKey(refreshToken));
    }

    private String createKey(String refreshToken) {
        return REFRESH_TOKEN_KEY_PREFIX + jwtUtil.extractUserIdFromToken(refreshToken);
    }
}
