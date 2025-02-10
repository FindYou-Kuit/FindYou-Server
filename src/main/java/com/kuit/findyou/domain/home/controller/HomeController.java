package com.kuit.findyou.domain.home.controller;

import com.kuit.findyou.domain.home.dto.GetHomeDataResponse;
import com.kuit.findyou.domain.home.service.HomeService;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/home")
public class HomeController {
    private final HomeService homeService;

    @Operation(
            summary = "홈화면 조회",
            description = "홈화면에서 보여줄 데이터를 조회할 수 있는 API입니다."
    )
    @GetMapping("/data")
    public BaseResponse<GetHomeDataResponse> getHomeData(){
        return new BaseResponse<>(homeService.getHomeData());
    }
}
