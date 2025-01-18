package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReportCardDTO {

    private List<Card> cards;
    private Long lastReportId;
    private Boolean isLast;

    public static ReportCardDTO newInstance(List<Card> cards, Long newLastReportId, Boolean isLast) {
        return ReportCardDTO.builder()
                .cards(cards)
                .lastReportId(newLastReportId)
                .isLast(isLast)
                .build();
    }
}
