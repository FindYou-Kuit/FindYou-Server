package com.kuit.findyou.global.common.jwt;

import com.kuit.findyou.global.common.exception.BadRequestException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        Exception exception = (Exception) request.getAttribute("exception");

        // 토큰이 유효하지 않은 경우를 제외하고, 잘못된 형식으로 type 으로 요청을 보낸 경우, 잘못된 메서드로 요청을 보낸 경우, 토큰이 누락된 경우 BadRequestException 이 터지도록 설정
        if(exception == null) {
            exception = new BadRequestException(BAD_REQUEST);
        }

        resolver.resolveException(request, response, null, exception);
    }
}
