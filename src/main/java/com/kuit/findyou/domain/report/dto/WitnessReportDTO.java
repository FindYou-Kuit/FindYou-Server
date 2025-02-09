package com.kuit.findyou.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WitnessReportDTO {
    @Schema(description = "업로드한 이미지 url 리스트")
    private List<String> imageUrls;
    @Schema(description = "축종", example = "개")
    private String species;
    @Schema(description = "품종", example = "말티즈")
    private Long breed;
    @Schema(description = "성별", example = "M")
    private String sex;
    @Schema(description = "털색")
    private List<String> furColor;
    @Schema(description = "목격 장소")
    private String location;
    @Schema(description = "특징 ID 리스트")
    private List<Long> features;
    @Schema(description = "추가 설명")
    private String description;
    @Schema(description = "목격 날짜" , example = "2025-02-07")
    private LocalDate foundDate;
    @Schema(description = "유저 ID")
    private Long userId;
}
