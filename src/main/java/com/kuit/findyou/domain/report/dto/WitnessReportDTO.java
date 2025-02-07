package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WitnessReportDTO {
    private List<String> imageUrls;
    private String spieces;
    private Long breed;
    private String sex;
    private List<String> furColor;
    private String location;
    private List<Long> features;
    private String description;
    private LocalDate foundDate;
    private Long userId;

}
