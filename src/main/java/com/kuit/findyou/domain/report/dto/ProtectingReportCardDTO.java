package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProtectingReportCardDTO {

    private List<Card> cards;
    private Long lastProtectId;
    private Boolean isLast;

    public static ProtectingReportCardDTO newInstance(List<Card> cards, Long newLastProtectId, Boolean isLast) {
        return ProtectingReportCardDTO.builder()
                .cards(cards)
                .lastProtectId(newLastProtectId)
                .isLast(isLast)
                .build();
    }
}
