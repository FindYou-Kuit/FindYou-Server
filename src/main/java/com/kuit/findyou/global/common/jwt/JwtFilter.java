package com.kuit.findyou.global.common.jwt;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.global.common.exception.InvalidTokenException;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.kuit.findyou.global.common.jwt.Header.*;
import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.extractTokenFromHeader(request, AUTHORIZATION.getKey());

        if (accessToken == null) {
            request.setAttribute("exception", new InvalidTokenException(TOKEN_NOT_EXIST));
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 예외 처리
            if (jwtUtil.validateToken(accessToken)) {
                Long userId = jwtUtil.extractUserIdFromToken(accessToken);
                setAuthenticationToContext(userId);
            }
        } catch (InvalidTokenException e) { // 여기서 던지는 에러를 CustomAuthenticationEntryPoint 가 잡아냄
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * SecurityContextHolder 에 Authentication 등록
     *
     * @param userId
     */
    private void setAuthenticationToContext(Long userId) {
        User loginUser = User.builder()
                .id(userId)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(loginUser);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
