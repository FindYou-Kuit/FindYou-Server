package com.kuit.findyou.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import com.kuit.findyou.global.common.response.BaseResponse;
import com.kuit.findyou.global.security.CustomUserDetails;
import com.kuit.findyou.global.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.LOGIN_FAILED;
import static com.kuit.findyou.global.jwt.constant.JwtAuthParameters.LOGIN_ENDPOINT;
import static com.kuit.findyou.global.jwt.constant.JwtAuthParameters.PARAMETER_MAPPED_TO_USERNAME;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    private static final String JWT_HEADER_KEY = "Authorization";
    private static final String JWT_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl(LOGIN_ENDPOINT.getValue());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter(PARAMETER_MAPPED_TO_USERNAME.getValue());
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = customUserDetails.getUserId();

        log.info("[successfulAuthentication] login success");

        String token = jwtUtil.createAccessJwt(userId);

        response.addHeader(JWT_HEADER_KEY, JWT_PREFIX + token);
        writeResponse(response, HttpServletResponse.SC_OK, CONTENT_TYPE, new BaseResponse<>(null));
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
