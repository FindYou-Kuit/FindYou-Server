package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.report.model.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Builder
@ToString
public class UserReportCard {
    private Long reportId;
    private String thumbnailImageUrl;
    private String title;
    private String tag;
    private LocalDate date;
    private String location;

    public static UserReportCard entityToDto(Report report) {
        return UserReportCard.builder()
                .reportId(report.getId())
                .thumbnailImageUrl(report.getThumbnailImage())
                .title(report.getReportAnimal().getBreed().getSpeciesAndBreed())
                .tag(report.getTag())
                .date(report.getEventDate())
                .location(report.getEventLocation())
                .build();
    }
}
