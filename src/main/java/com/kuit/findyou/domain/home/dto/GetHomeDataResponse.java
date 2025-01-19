package com.kuit.findyou.domain.home.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class GetHomeDataResponse {
    private Long todayRescuedAnimalCount;
    private Long todayReportAnimalCount;
    private List<HomeProtectAnimalCard> protectAnimalCards;
    private List<HomeReportAnimalCard> reportAnimalCards;
}
