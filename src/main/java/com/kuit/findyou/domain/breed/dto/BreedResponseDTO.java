package com.kuit.findyou.domain.breed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BreedResponseDTO {

    @Schema(description = "품종 ID")
    private Long breedId;

    @Schema(description = "품종 이름", example = "시츄")
    private String breedName;

    @Schema(description = "축종", example = "개")
    private String species;

}
