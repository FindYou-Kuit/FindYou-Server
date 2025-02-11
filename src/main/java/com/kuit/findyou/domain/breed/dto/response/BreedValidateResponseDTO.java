package com.kuit.findyou.domain.breed.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BreedValidateResponseDTO {

    @Schema(description = "품종 ID")
    private Long breedId;

    @Schema(description = "DB에 존재하는 품종인지 여부")
    private Boolean isExist;
}
