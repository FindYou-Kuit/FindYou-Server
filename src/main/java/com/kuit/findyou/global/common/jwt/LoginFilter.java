package com.kuit.findyou.global.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.findyou.domain.auth.service.RedisService;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import com.kuit.findyou.global.common.response.BaseResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

import static com.kuit.findyou.global.common.jwt.Header.*;
import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.LOGIN_FAILED;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트의 요청에서 kakaoId 추출
        String kakaoId = request.getParameter("kakaoId");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(kakaoId, "password", Collections.emptyList());

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        Long userId = customUserDetails.getUserId();

        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        // DB에 Refresh 토큰 저장
        redisService.saveRefreshToken(refreshToken);

        // 응답 헤더 설정
        response.addHeader(AUTHORIZATION.getKey(), BEARER.getKey() + accessToken);
        response.addHeader(REFRESH.getKey(), BEARER.getKey() + refreshToken);

        // BaseResponse 생성
        BaseResponse<Void> baseResponse = new BaseResponse<>(null);

        // JSON 변환
        String successResponse = new ObjectMapper().writeValueAsString(baseResponse);

        // 응답 설정
        createResponse(response, HttpServletResponse.SC_OK, "application/json", successResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        // BaseErrorResponse 생성
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse(LOGIN_FAILED);

        // JSON 변환
        String unSuccessResponse = new ObjectMapper().writeValueAsString(baseErrorResponse);

        // 응답 설정
        createResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "application/json", unSuccessResponse);
    }


    private void createResponse(HttpServletResponse response, int status, String contentType, String body) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        response.getWriter().write(body);
    }
}
