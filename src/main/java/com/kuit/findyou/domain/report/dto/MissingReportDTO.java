package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MissingReportDTO {
    private List<String> imageUrls;
    private String spieces;
    private Long breed;
    private String sex;
    private List<String> furColor;
    private String location;
    private List<Long> features;
    private String description;
    private LocalDate missingDate;
    private Long userId;


    public Long getBreedId() {
        return breed; // 품종 id로 반환
    }

    public List<Long> getFeatureIds() {
        return features; //특징 id 리스트로 반환
    }

}
