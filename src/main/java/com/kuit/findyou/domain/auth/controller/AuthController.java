package com.kuit.findyou.domain.auth.controller;

import com.kuit.findyou.domain.auth.dto.request.SignUpRequestDTO;
import com.kuit.findyou.domain.auth.service.AuthService;
import com.kuit.findyou.global.common.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public BaseResponse<Void> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);

        return new BaseResponse<>(null);
    }

    @PostMapping("/reissue")
    public BaseResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);

        return new BaseResponse<>(null);
    }

    @PostMapping("/oauth/kakao")
    public BaseResponse<Void> kakaoLogin(HttpServletResponse response, @RequestBody SignUpRequestDTO kakaoLoginRequest) {
        authService.kakaoLogin(response, kakaoLoginRequest);

        return new BaseResponse<>(null);
    }

}
