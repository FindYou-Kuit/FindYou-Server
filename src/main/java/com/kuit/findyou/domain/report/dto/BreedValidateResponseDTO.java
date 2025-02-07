package com.kuit.findyou.domain.report.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BreedValidateResponseDTO {

    private Long breedId;

    private Boolean isExist;
}
