package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.model.ReportedAnimalFeature;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ReportInfoDTO {

    private List<String> imageUrls;
    private String tag;
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
        for(ReportedAnimalFeature reportedAnimalFeature : reportAnimal.getReportedAnimalFeatures()) {
            animalFeatureList.add(reportedAnimalFeature.getFeature().getFeatureValue());
        }

        // 신고자 정보
        User reportUser = report.getUser();

        Boolean interest = loginedUser.isInterestReport(report.getId());

        List<String> tempImages = new ArrayList<>();  // 이미지 관련 로직이 아직 없어서 더미 데이터 삽입
        tempImages.add("image1.jpg");
        tempImages.add("image2.jpg");
        tempImages.add("image3.jpg");

        return ReportInfoDTO.builder()
                .imageUrls(tempImages)   // 더미 데이터임
                .tag(report.getTag())
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
