package com.kuit.findyou.domain.auth.controller;

import com.kuit.findyou.domain.auth.dto.SignupRequest;
import com.kuit.findyou.domain.auth.service.UserSignupService;
import com.kuit.findyou.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserSignupService userSignupService;

    @GetMapping("test")
    public BaseResponse<String> test(){
        return new BaseResponse<>("auth controller");
    }

    @PostMapping("signup")
    public BaseResponse<Void> signup(@RequestBody SignupRequest request){
        log.info("[signup] email = {} password = {}", request.getEmail(), request.getPassword());
        userSignupService.signup(request.getEmail(), request.getPassword());
        return new BaseResponse<>(null);
    }
}
