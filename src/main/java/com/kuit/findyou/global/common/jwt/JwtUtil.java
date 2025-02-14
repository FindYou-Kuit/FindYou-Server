package com.kuit.findyou.global.common.jwt;

import com.kuit.findyou.global.common.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.kuit.findyou.global.common.jwt.Header.*;
import static com.kuit.findyou.global.common.jwt.TokenType.*;
import static com.kuit.findyou.global.common.jwt.TokenType.REFRESH;
import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Getter
@Component
@Slf4j
public class JwtUtil {

    private Key secretKey;

    @Value("${spring.jwt.access-token-expiration}")
    private Long access_token_expiration;

    @Value("${spring.jwt.refresh-token-expiration}")
    private Long refresh_token_expiration;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(byteSecretKey);
    }

    /**
     * Access 토큰 발급
     *
     * @param userId
     * @return
     */
    public String createAccessToken(Long userId) {

        Claims claims = Jwts.claims();
        claims.put("type", ACCESS.getType());
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + access_token_expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 발급
     * Refresh 토큰에는 많은 정보가 들어갈 필요가 없음
     *
     * @return
     */
    public String createRefreshToken(Long userId) {

        Claims claims = Jwts.claims();
        claims.put("type", REFRESH.getType());
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refresh_token_expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 헤더에서 토큰 추출
     *
     * @param request
     * @param header
     * @return
     */
    public String extractTokenFromHeader(HttpServletRequest request, String header) {
        return Optional.ofNullable(request.getHeader(header))
                .filter(token -> token.startsWith(BEARER.getKey()))
                .map(token -> token.split(" ")[1])
                .orElse(null);
    }

    /**
     * 토큰에서 유저 아이디 추출
     *
     * @param token
     * @return
     */
    public Long extractUserIdFromToken(String token) {
        Long userId = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("userId", Long.class);

        if (userId == null) {
            throw new InvalidTokenException(NO_USERINFO_IN_TOKEN);
        }

        return userId;
    }

    /**
     * 토큰에서 타입 추출
     *
     * @param token
     * @return
     */
    public String extractTypeFromToken(String token) {
        String type = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("type", String.class);

        if (type == null) {
            throw new InvalidTokenException(NO_TYPE_IN_TOKEN);
        }

        return type;
    }

    /**
     * 토큰 검증
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
            throw new InvalidTokenException(EXPIRED_TOKEN);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 토큰입니다.", e);
            throw new InvalidTokenException(INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
            throw new InvalidTokenException(NOT_SUPPORTED_TOKEN);
        }
    }


}
