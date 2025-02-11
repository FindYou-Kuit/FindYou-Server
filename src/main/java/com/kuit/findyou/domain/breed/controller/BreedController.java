package com.kuit.findyou.domain.breed.controller;

import com.kuit.findyou.domain.breed.dto.BreedResponseDTO;
import com.kuit.findyou.domain.breed.dto.BreedValidateResponseDTO;
import com.kuit.findyou.domain.breed.service.BreedValidateService;
import com.kuit.findyou.domain.breed.service.GetAllBreedsService;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/breeds")
public class BreedController {

    private final GetAllBreedsService getAllBreedsService;
    private final BreedValidateService breedValidateService;

    @Operation(summary = "품종 전체 반환", description = "모든 품종 정보를 반환합니다.")
    @GetMapping
    public BaseResponse<List<BreedResponseDTO>> getAllBreeds() {
        return new BaseResponse<>(getAllBreedsService.getAllBreeds());
    }

    @Operation(summary = "품종 검증", description = "입력으로 전달된 품종이 DB에 존재하는지 검증합니다.")
    @GetMapping("/validation")
    public BaseResponse<BreedValidateResponseDTO> validateBreed(
            @Parameter(required = true, description = "DB에 존재하는 지 검증할 품종")
            @RequestParam String breedName) {
        return new BaseResponse<>(breedValidateService.validateBreed(breedName));
    }

}
