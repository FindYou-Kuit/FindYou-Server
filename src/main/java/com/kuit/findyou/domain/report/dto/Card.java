package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Card {

    private Long cardId;
    private String thumbnailImageUrl;
    private String title;
    private ReportTag tag;
    private String date;
    private String location;
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
