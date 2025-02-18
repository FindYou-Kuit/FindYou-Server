package com.kuit.findyou.global.jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;
import static com.kuit.findyou.global.jwt.constant.JwtAuthParameters.*;

@Slf4j
public class JsonLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private JwtUtil jwtUtil;
    private ObjectMapper objectMapper;

    public JsonLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(LOGIN_ENDPOINT.getValue(), authenticationManager);
        this.objectMapper = new ObjectMapper();
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("json login filter");
        Map<String, String> params = readJsonData(request);
        String email = params.getOrDefault("email", null);
        String password = params.getOrDefault("password", null);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private Map<String, String> readJsonData(HttpServletRequest request) throws IOException {
        if(!request.getMethod().equals("POST") || request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            return new HashMap<>();
        }
        return objectMapper.readValue(request.getInputStream(), Map.class);
    }

    private String getKakaoId(HttpServletRequest request) throws IOException {
        if(!request.getMethod().equals("POST") || request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            return null;
        }
        Map<String, String> map = objectMapper.readValue(request.getInputStream(), Map.class);
        String kakaoId = map.getOrDefault(PARAMETER_MAPPED_TO_USERNAME.getValue(), null);
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
