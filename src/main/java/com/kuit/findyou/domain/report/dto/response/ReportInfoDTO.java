package com.kuit.findyou.domain.report.dto.response;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.image.model.Image;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.model.ReportedAnimalFeature;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReportInfoDTO {

    @Schema(description = "이미지 url들")
    @Builder.Default
    private List<String> imageUrls = null;

    @Schema(description = "태그", example = "목격신고")
    private ReportTag tag;

    @Schema(description = "성별", example = "암컷")
    private String sex;

    @Schema(description = "축종 + 품종 정보", example = "[개] 웰시코기")
    private String breed;

    @Schema(description = "털색", example = "갈색, 흰색")
    private String furColor;

    @Schema(description = "목격자 or 신고자 정보", example = "홍길동")
    private String userName;

    @Schema(description = "작성 날짜", example = "2024-11-05")
    private String writeDate;

    @Schema(description = "발견 날짜 or 실종 날짜", example = "2024-11-05")
    private String eventDate;

    @Schema(description = "발견 장소 or 실종 장소", example = "광진구 화양동")
    private String eventLocation;

    @Schema(description = "특징 정보들")
    private List<String> features;

    @Schema(description = "추가 정보들", example = "사람과 컨택하는 거는 무서워 하는 것....")
    private String additionalDescription;

    @Schema(description = "관심 신고글 여부")
    private Boolean interest;


    public static ReportInfoDTO newInstanceFromReportWithUser(Report report, User loginedUser) {
        // 신고 동물 정보
        ReportAnimal reportAnimal = report.getReportAnimal();

        // 동물 특징 정보들
        List<String> animalFeatureList = new ArrayList<>();
        for (ReportedAnimalFeature reportedAnimalFeature : reportAnimal.getReportedAnimalFeatures()) {
            animalFeatureList.add(reportedAnimalFeature.getFeature().getFeatureValue());
        }

        // 신고자 정보
        User reportUser = report.getUser();

        Boolean interest = loginedUser.isInterestReport(report.getId());

        return ReportInfoDTO.builder()
                .imageUrls(
                        report.getImages().stream()     // Image 리스트를 받아서 FilePath를 반환
                                .map(Image::getFilePath)
                                .collect(Collectors.toList()))
                .tag(report.getTag())
                .sex(reportAnimal.getSex())
                .breed(reportAnimal.getBreed().getSpeciesAndBreed())
                .furColor(reportAnimal.getFurColor())
                .userName(reportUser.getName())
                .writeDate(report.getCreatedAt().toLocalDate().toString())
                .eventDate(report.getEventDate().toString())
                .eventLocation(report.getEventLocation())
                .features(animalFeatureList)
                .additionalDescription(report.getAdditionalDescription())
                .interest(interest)
                .build();
    }
}
