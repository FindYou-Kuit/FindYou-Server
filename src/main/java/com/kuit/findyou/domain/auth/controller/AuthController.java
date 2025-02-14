package com.kuit.findyou.domain.auth.controller;

import com.kuit.findyou.domain.auth.dto.SignupRequest;
import com.kuit.findyou.domain.auth.service.UserSignupService;
import com.kuit.findyou.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    UserSignupService userSignupService;

    @GetMapping("test")
    public BaseResponse<String> test(){
        return new BaseResponse<>("auth controller");
    }

    @PostMapping("signup")
    public BaseResponse<Void> signup(SignupRequest request){
        userSignupService.signup(request.getEmail(), request.getPassword());
        return new BaseResponse<>(null);
    }
}
