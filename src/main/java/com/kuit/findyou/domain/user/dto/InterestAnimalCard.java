package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import com.kuit.findyou.domain.report.model.InterestReport;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InterestAnimalCard {
    private Long animalId;
    private String thumbnailImageUrl;
    private String title;
    private String tag;
    private LocalDate date;
    private String location;
    private Boolean interest;

    public static InterestAnimalCard from(InterestProtectingReport interestProtect) {
        return InterestAnimalCard.builder()
                .animalId(interestProtect.getProtectingReport().getId())
                .thumbnailImageUrl(interestProtect.getProtectingReport().getImageUrl())
                .title(interestProtect.getProtectingReport().getBreed())
                .tag(ReportTag.PROTECTING.getValue())
                .date(interestProtect.getProtectingReport().getHappenDate())
                .location(interestProtect.getProtectingReport().getCareAddr())
                .interest(true)
                .build();
    }

    public static InterestAnimalCard from(InterestReport interestReport) {
        return InterestAnimalCard.builder()
                .animalId(interestReport.getReport().getId())
                .thumbnailImageUrl(interestReport.getReport().getImages().size() > 0 ? interestReport.getReport().getImages().get(0).getFilePath() : null)
                .title(interestReport.getReport().getReportAnimal().getBreedName())
                .tag(interestReport.getReport().getTag())
                .date(interestReport.getReport().getEventDate())
                .location(interestReport.getReport().getEventLocation())
                .interest(true)
                .build();
    }
}
