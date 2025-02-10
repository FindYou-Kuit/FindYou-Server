package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "사용자의 신고내역")
@Getter
@Builder
@ToString
public class UserReportCard {
    @Schema(description = "신고 동물의 id")
    private Long reportId;

    @Schema(description = "썸네일 이미지 url")
    private String thumbnailImageUrl;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "태그. 값은 목격신고, 실종신고 중 하나임")
    private ReportTag tag;

    @Schema(description = "날짜. 실종날짜 또는 목격날짜")
    private LocalDate date;

    @Schema(description = "장소. 실종장소 혹은 목격장소")
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
