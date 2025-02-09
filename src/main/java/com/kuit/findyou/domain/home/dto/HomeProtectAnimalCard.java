package com.kuit.findyou.domain.home.dto;

import com.kuit.findyou.domain.report.model.ProtectingReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Schema(description = "홈화면 보호중동물 조회의 응답 DTO")
@Getter
@Builder
public class HomeProtectAnimalCard {
    @Schema(description = "보호중동물 id")
    private Long protectId;

    @Schema(description = "썸네일 이미지 url")
    private String thumbnailImageUrl;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "태그", example = "보호중")
    private String tag;

    @Schema(description = "접수일")
    private LocalDate happenDate;

    @Schema(description = "보호장소")
    private String careAddress;

    public static HomeProtectAnimalCard entityToDto(ProtectingReport entity){
        return HomeProtectAnimalCard.builder()
                .protectId(entity.getId())
                .thumbnailImageUrl(entity.getImageUrl())
                .title(entity.getBreed())
                .tag(ReportTag.PROTECTING.getValue())
                .happenDate(entity.getHappenDate())
                .careAddress(entity.getCareAddr())
                .build();
    }
}
