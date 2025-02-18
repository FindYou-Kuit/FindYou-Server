package com.kuit.findyou.domain.auth.controller;

import com.kuit.findyou.domain.auth.dto.CheckDuplicateEmailRequest;
import com.kuit.findyou.domain.auth.dto.CheckDuplicateEmailResponse;
import com.kuit.findyou.domain.auth.dto.SignupRequest;
import com.kuit.findyou.domain.auth.service.UserSignupService;
import com.kuit.findyou.global.common.response.BaseResponse;
import jakarta.validation.Valid;
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
    public BaseResponse<Void> signup(@RequestBody @Valid SignupRequest request){
        log.info("[signup] email = {} password = {}", request.getEmail(), request.getPassword());
        userSignupService.signup(request);
        return new BaseResponse<>(null);
    }

    @PostMapping("check/duplicate-email")
    public BaseResponse<CheckDuplicateEmailResponse> checkDuplicateEmail(@RequestBody @Valid CheckDuplicateEmailRequest request){
        Boolean isDuplicate = userSignupService.checkDuplicateEmail(request.getEmail());
        return new BaseResponse<>(new CheckDuplicateEmailResponse(isDuplicate));
    }
}
