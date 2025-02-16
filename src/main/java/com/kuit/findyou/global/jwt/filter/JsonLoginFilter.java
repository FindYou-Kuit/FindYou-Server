package com.kuit.findyou.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import com.kuit.findyou.global.common.response.BaseResponse;
import com.kuit.findyou.global.jwt.util.JwtUtil;
import com.kuit.findyou.global.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
public class JsonLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final String CONTENT_TYPE = "application/json";
    private static final String DEFAULT_FILTER_PROCESSES_URL = "/api/v1/auth/login/kakao";
    private static final String JSON_PARAMETER_MAPPED_TO_USERNAME = "kakaoId";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String ENCODING = "UTF-8";
    private JwtUtil jwtUtil;
    private ObjectMapper objectMapper;

    public JsonLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(DEFAULT_FILTER_PROCESSES_URL, authenticationManager);
        this.objectMapper = new ObjectMapper();
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("json login filter");
        String kakaoId = getKakaoId(request);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(kakaoId, DEFAULT_PASSWORD);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String getKakaoId(HttpServletRequest request) throws IOException {
        if(!request.getMethod().equals("POST") || request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            return null;
        }
        Map<String, String> map = objectMapper.readValue(request.getInputStream(), Map.class);
        String kakaoId = map.getOrDefault(JSON_PARAMETER_MAPPED_TO_USERNAME, null);
        return kakaoId;
    }

    //로그인 성공시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = customUserDetails.getUserId();

        log.info("[successfulAuthentication] login success");

        String accessToken = jwtUtil.createAccessJwt(userId);

        Map<String, String> resp = new HashMap<>();
        resp.put("accessToken", accessToken);
        writeResponse(response, HttpServletResponse.SC_OK, CONTENT_TYPE, new BaseResponse<>(resp));
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, CONTENT_TYPE, new BaseErrorResponse(LOGIN_FAILED));
    }

    private void writeResponse(HttpServletResponse response, int status, String contentType, Object value) throws IOException {
        response.setStatus(status);
        response.setContentType(contentType);
        response.setCharacterEncoding(ENCODING);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(value);
        response.getWriter().write(body);
    }
}
