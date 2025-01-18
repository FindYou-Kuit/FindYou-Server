package com.kuit.findyou.domain.home.dto;

import com.kuit.findyou.domain.report.model.ProtectingReport;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class HomeProtectAnimalCard {
    private Long protectId;
    private String thumbnailImageUrl;
    private String title;
    private String tag;
    private LocalDate noticeStartDate;
    private String careAddress;

    public static HomeProtectAnimalCard entityToDto(ProtectingReport entity){
        return HomeProtectAnimalCard.builder()
                .protectId(entity.getId())
                .thumbnailImageUrl(entity.getImageUrl())
                .title(entity.getBreed())
                .tag(ReportTag.PROTECTING.getValue())
                .noticeStartDate(entity.getNoticeStartDate())
                .careAddress(entity.getCareAddr())
                .build();
    }
}
