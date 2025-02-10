package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import com.kuit.findyou.domain.report.model.InterestReport;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Schema(description = "관심 동물 정보를 담은 DTO객체")
@Getter
@Builder
public class InterestAnimalCard {
    @Schema(description = "동물의 id. tag가 보호중이면 protectingReportId, protectingAnimalId로 해석, tag가 실종신고 또는 목격 신고이면 reportId 또는 reportAnimalId로 해석 ")
    private Long animalId;
    @Schema(description = "썸네일 이미지 url ")
    private String thumbnailImageUrl;
    @Schema(description = "보여줄 카드의 제목 ")
    private String title;
    @Schema(description = "보여줄 카드의 태그. 값은 보호중, 실종신고, 목격신고 중 하나 ")
    private ReportTag tag;
    @Schema(description = "날짜. 보호중동물의 경우 구조 날짜, 신고동물의 경우 목격날짜 혹은 신고날짜로 해석")
    private LocalDate date;
    @Schema(description = "위치. 보호중동물의 경우 보호기관의 주소, 신고동물의 경우 목격장소 혹은 발견장소로 해석")
    private String location;
    @Schema(description = "관심 동물로 등록했는지 여부")
    private Boolean interest;

    public static InterestAnimalCard from(InterestProtectingReport interestProtect) {
        return InterestAnimalCard.builder()
                .animalId(interestProtect.getProtectingReport().getId())
                .thumbnailImageUrl(interestProtect.getProtectingReport().getImageUrl())
                .title(interestProtect.getProtectingReport().getBreed())
                .tag(ReportTag.PROTECTING)
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
