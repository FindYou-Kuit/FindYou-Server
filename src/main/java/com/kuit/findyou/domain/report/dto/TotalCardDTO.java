package com.kuit.findyou.domain.report.dto;

import lombok.*;

import java.util.Collections;
import java.util.List;


@Getter
@Builder
public class TotalCardDTO {

    private List<Card> cards;
    private Long lastProtectId;
    private Long lastReportId;
    private Boolean isLast;

    public static TotalCardDTO newInstanceWithShuffle(List <Card> cards, Long newLastProtectId, Long newLastReportId, Boolean isLast){
        Collections.shuffle(cards);

        return TotalCardDTO.builder()
                .cards(cards)
                .lastProtectId(newLastProtectId)
                .lastReportId(newLastReportId)
                .isLast(isLast)
                .build();

    }

}
