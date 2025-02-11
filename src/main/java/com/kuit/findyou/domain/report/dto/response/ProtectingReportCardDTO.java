package com.kuit.findyou.domain.report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProtectingReportCardDTO {

    @Schema(description = "보호글들에 대한 카드 리스트")
    private List<Card> cards;

    @Schema(description = "페이징을 통해 반환된 보호글 중 마지막 보호글의 ID. 다음 요청에 이 값을 전달함으로써 무한스크롤을 구현합니다.")
    private Long lastProtectId;

    @Schema(description = "마지막 데이터인지 여부")
    private Boolean isLast;

    public static ProtectingReportCardDTO newInstance(List<Card> cards, Long newLastProtectId, Boolean isLast) {
        return ProtectingReportCardDTO.builder()
                .cards(cards)
                .lastProtectId(newLastProtectId)
                .isLast(isLast)
                .build();
    }
}
