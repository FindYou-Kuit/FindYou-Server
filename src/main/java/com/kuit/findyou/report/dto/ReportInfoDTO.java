package com.kuit.findyou.report.dto;

import lombok.Builder;
import lombok.Getter;

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
    private String foundLocation;
    private List<String> features;
    private String additionalDescription;
    private Boolean interest;
}
