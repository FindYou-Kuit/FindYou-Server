package com.kuit.findyou.domain.auth;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Operation(summary = "테스트", description = "테스트")
    @GetMapping("test")
    public String hello(){
        return "hello";
    }
}
