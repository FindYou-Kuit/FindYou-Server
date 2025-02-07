package com.kuit.findyou.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class BreedResponseDTO {

    private Long breedId;

    private String breedName;

    private String species;

}
