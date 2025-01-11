package com.kuit.findyou.domain.auth;

import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Operation(summary = "테스트", description = "테스트")
    @GetMapping("test")
    public BaseResponse<String> hello(){
        return new BaseResponse<>("hello");
    }

    @GetMapping("test/error")
    public BaseResponse<String> testControllerAdvice(){
//        if(true) throw new BadRequestException(BAD_REQUEST);
        if(true) throw new IllegalArgumentException();
        return new BaseResponse<>("hello");
    }
}
