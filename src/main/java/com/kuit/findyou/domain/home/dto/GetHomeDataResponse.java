package com.kuit.findyou.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class GetHomeDataResponse {
    @Schema(description = "어제 구조된 동물의 수")
    private Long yesterdayRescuedAnimalCount;

    @Schema(description = "어제 신고된 동물의 수.")
    private Long yesterdayReportedAnimalCount;

    @Schema(description = "보호중동물 리스트")
    private List<HomeProtectAnimalCard> protectAnimalCards;

    @Schema(description = "신고동물 리스트")
    private List<HomeReportAnimalCard> reportAnimalCards;
}
