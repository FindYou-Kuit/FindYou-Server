package com.kuit.findyou.global.config;

import com.kuit.findyou.global.jwt.filter.JsonLoginFilter;
import com.kuit.findyou.global.logging.MDCLoggingFilter;
import com.kuit.findyou.global.security.JwtAuthenticationEntryPoint;
import com.kuit.findyou.global.jwt.filter.JwtFilter;
import com.kuit.findyou.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.kuit.findyou.global.jwt.constant.JwtAuthParameters.LOGIN_ENDPOINT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtUtil jwtUtil;
    private final MDCLoggingFilter mdcLoggingFilter;

    private static final String[] PERMIT_URL = {
            LOGIN_ENDPOINT.getValue(), "api/v1/auth/signup", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/index.html", "/actuator/health"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth)->auth.disable());

        http
                .httpBasic((auth)->auth.disable());

        // 토큰 기반 인증 비활성화
        http
                .authorizeHttpRequests((auth)-> auth
                        .anyRequest().permitAll());

        // 토큰 기반 인증 활성화
//        http
//                .authorizeHttpRequests((auth)-> auth
//                        .requestMatchers(PERMIT_URL).permitAll()
//                        .anyRequest().authenticated());

        // MDC 필터 등록
        http
                .addFilterBefore(mdcLoggingFilter, UsernamePasswordAuthenticationFilter.class);

        // 토큰 검증 필터 추가
        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // form 방식의 로그인 필터 추가
//        http
//                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // json 방식의 로그인 필터 추가
        http
                .addFilterAt(new JsonLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 인증 실패시 예외 처리
        http

                .exceptionHandling(handler-> handler
                                .authenticationEntryPoint(entryPoint));

        http
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("https://findyou.store"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
