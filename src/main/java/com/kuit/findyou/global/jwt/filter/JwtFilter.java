package com.kuit.findyou.global.jwt.filter;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.global.common.exception.JwtNotFoundException;
import com.kuit.findyou.global.jwt.exception.InvalidJwtException;
import com.kuit.findyou.global.security.CustomUserDetails;
import com.kuit.findyou.global.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final String JWT_HEADER_KEY = "Authorization";
    private static final String JWT_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 토큰 추출
        try{
            String token = extractToken(request);
            if(token == null){
                throw new JwtNotFoundException(TOKEN_NOT_FOUND);
            }

            // 토큰 검증
            if(!jwtUtil.validateJwt(token)) {
                throw new InvalidJwtException(INVALID_TOKEN);
            }

            // 토큰을 request에 저장하여 entryPoint에 전달
            request.setAttribute("token", token);

            // UserDetails 생성
            String username = jwtUtil.getUsername(token);

            // jwt 인증 시 user_id 를 MDC에 삽입
            Long userId = jwtUtil.getUserId(token);
            MDC.put("user_id", String.valueOf(userId));


            User user = User.builder()
                    .email(username)
                    .password("password")
                    .build();
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            //스프링 시큐리티 인증 토큰 생성하고 세션에 저장
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        catch(Exception e){
            log.info("token authentication failure");
            request.setAttribute("exception", e);
        }
        finally {
            MDC.clear();
        }
        filterChain.doFilter(request, response);
    }

    private static String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader(JWT_HEADER_KEY);
        if (authorization != null && authorization.startsWith(JWT_PREFIX)) {
            return authorization.split(" ")[1];
        }
        log.info("token does not exist");
        return null;
    }
}




