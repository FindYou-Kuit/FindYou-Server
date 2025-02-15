package com.kuit.findyou.global.common.jwt;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.global.common.exception.JwtNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.TOKEN_NOT_FOUND;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");

            //Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new JwtNotFoundException(TOKEN_NOT_FOUND);
            }

            System.out.println("authorization now");
            String token = authorization.split(" ")[1];

            //토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {

                System.out.println("token expired");
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료
                return;
            }

            request.setAttribute("token", token);

            String username = jwtUtil.getUsername(token);

            User user = User.builder()
                    .email(username)
                    .password("temppassword")
                    .build();

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        catch(Exception e){
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
}




