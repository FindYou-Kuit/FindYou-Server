package com.kuit.findyou.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReportCardDTO {

    @Schema(description = "신고글들에 대한 카드 리스트")
    private List<Card> cards;

    @Schema(description = "페이징을 통해 반환된 보호글 중 마지막 신고글의 ID. 다음 요청에 이 값을 전달함으로써 무한스크롤을 구현합니다.")
    private Long lastReportId;

    @Schema(description = "마지막 데이터인지 여부")
    private Boolean isLast;

    public static ReportCardDTO newInstance(List<Card> cards, Long newLastReportId, Boolean isLast) {
        return ReportCardDTO.builder()
                .cards(cards)
                .lastReportId(newLastReportId)
                .isLast(isLast)
                .build();
    }
}
