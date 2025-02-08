package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.report.dto.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "사용자의 신고내역 조회의 응답 DTO")
@Getter
@Builder
public class GetUsersReportsResponse {
    @Schema(description = "사용자의 신고내역 리스트")
    private List<UserReportCard> reports;

    @Schema(description = "마지막으로 조회된 신고동물의 id. 다음 요청에 전달해야 함")
    private Long lastReportId;

    @Schema(description = "마지막 페이지 여부")
    private Boolean isLast;
}
