package com.kuit.findyou.global.config;

import com.kuit.findyou.domain.auth.service.RedisService;
import com.kuit.findyou.global.common.jwt.CustomAuthenticationEntryPoint;
import com.kuit.findyou.global.common.jwt.JwtFilter;
import com.kuit.findyou.global.common.jwt.JwtUtil;
import com.kuit.findyou.global.common.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final RedisService redisService;

    private static final String[] PERMIT_URL = {
            "/api/v1/auth/login", "/api/v1/auth/signUp", "/api/v1/auth/reissue", "/api/v1/breeds", "/api/v1/breeds/validation",
            "/api/v1/auth/oauth/kakao", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html","/swagger-ui/index.html"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        // Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(PERMIT_URL).permitAll()
                        .anyRequest().authenticated());

        // LoginFilter 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);

        // JwtFilter 등록
        http
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // 스프링 시큐리티 예외 처리 - EntryPoint 등록
        http
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint));

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
}
