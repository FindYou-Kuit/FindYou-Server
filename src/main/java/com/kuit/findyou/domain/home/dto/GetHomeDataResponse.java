package com.kuit.findyou.domain.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class GetHomeDataResponse {
    @Schema(description = "어제 구조된 동물의 수로 변경될 예정입니다.")
    private Long todayRescuedAnimalCount;

    @Schema(description = "오늘 신고된 동물의 수.")
    private Long todayReportAnimalCount;

    @Schema(description = "보호중동물 리스트")
    private List<HomeProtectAnimalCard> protectAnimalCards;

    @Schema(description = "신고동물 리스트")
    private List<HomeReportAnimalCard> reportAnimalCards;
}
