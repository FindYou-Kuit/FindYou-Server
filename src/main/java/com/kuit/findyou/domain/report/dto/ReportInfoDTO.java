package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.image.model.Image;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.model.ReportedAnimalFeature;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReportInfoDTO {

    @Builder.Default
    private List<String> imageUrls = null;
    private ReportTag tag;
    private String sex;
    private String breed;
    private String furColor;
    private String userName;
    private String writeDate;
    private String eventDate;
    private String eventLocation;
    private List<String> features;
    private String additionalDescription;
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
