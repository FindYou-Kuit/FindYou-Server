package com.kuit.findyou.domain.home.dto;

import com.kuit.findyou.domain.report.model.Report;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class HomeReportAnimalCard {
    private Long reportId;
    private String thumbnailImageUrl;
    private String title;
    private ReportTag tag;
    private LocalDateTime registerDate;
    private String happenLocation;

    public static HomeReportAnimalCard entityToDto(Report entity){
        return HomeReportAnimalCard.builder()
                .reportId(entity.getId())
                .thumbnailImageUrl("test-image.url")
                .title(entity.getReportAnimalBreedName())
                .tag(entity.getTag())
                .registerDate(entity.getCreatedAt())
                .happenLocation(entity.getEventLocation())
                .build();
    }
}
