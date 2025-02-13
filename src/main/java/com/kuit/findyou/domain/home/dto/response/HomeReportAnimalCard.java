package com.kuit.findyou.domain.home.dto.response;

import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Schema(description = "신고동물 응답 DTO")
@Getter
@Builder
public class HomeReportAnimalCard {
    @Schema(description = "신고동물의 id")
    private Long reportId;

    @Schema(description = "썸네일 이미지 url")
    private String thumbnailImageUrl;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "태그. 값은 목격신고 또는 실종신고임")
    private ReportTag tag;

    @Schema(description = "신고날짜")
    private LocalDate registerDate;

    @Schema(description = "목격장소 혹은 실종장소")
    private String happenLocation;

    public static HomeReportAnimalCard entityToDto(Report entity){
        return HomeReportAnimalCard.builder()
                .reportId(entity.getId())
                .thumbnailImageUrl("test-image.url")
                .title(entity.getReportAnimalBreedName())
                .tag(entity.getTag())
                .registerDate(entity.getCreatedAt().toLocalDate())
                .happenLocation(entity.getEventLocation())
                .build();
    }
}
