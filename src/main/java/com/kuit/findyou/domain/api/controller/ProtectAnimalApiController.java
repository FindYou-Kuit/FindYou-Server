package com.kuit.findyou.domain.api.controller;

import com.kuit.findyou.domain.api.service.ProtectAnimalApiUtil;
import com.kuit.findyou.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProtectAnimalApiController {

    private final ProtectAnimalApiUtil protectAnimalApiService;

    @GetMapping("/api/v1/initDatabase")
    public BaseResponse<Void> initDatabase() {

        protectAnimalApiService.updateAllProtectingReports();

        return new BaseResponse<>(null);
    }
}
