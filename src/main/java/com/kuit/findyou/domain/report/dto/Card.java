package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Card {

    @Schema(description = "카드의 Id. 태그에 따라 보호글 ID 인지, 신고글 ID 인지를 구분")
    private Long cardId;

    @Schema(description = "카드 정보에 보여질 썸네일 이미지 URL", example = "image1.png")
    private String thumbnailImageUrl;

    @Schema(description = "카드 제목", example = "말티즈")
    private String title;

    @Schema(description = "태그 정보",example = "목격신고")
    private ReportTag tag;

    @Schema(description = "날짜 정보", example = "2024-11-05")
    private String date;

    @Schema(description = "장소 정보", example = "서울특별시 광진구")
    private String location;

    @Schema(description = "관심글 여부")
    private Boolean interest;

    public static Card newInstanceFromReportWithUser(Report report, User loginedUser) {
        return Card.builder()
                .cardId(report.getId())
                .thumbnailImageUrl(
                        (report.getImages() == null || report.getImages().isEmpty())
                        ? null : report.getImages().get(0).getFilePath()
                )
                .title(report.getReportAnimal().getBreed().getName())
                .tag(report.getTag())
                .date(report.getEventDate().toString())
                .location(report.getEventLocation())
                .interest(loginedUser.isInterestReport(report.getId()))
                .build();
    }

    public static Card newInstanceFromProtectingReportWithUser(ProtectingReport protectingReport, User loginedUser) {
        return Card.builder()
                .cardId(protectingReport.getId())
                .thumbnailImageUrl(protectingReport.getImageUrl())
                .title(protectingReport.getBreed())
                .tag(ReportTag.PROTECTING)
                .date(protectingReport.getHappenDate().toString())
                .location(protectingReport.getCareAddr())
                .interest(loginedUser.isInterestProtectingReport(protectingReport.getId()))
                .build();
    }

}
