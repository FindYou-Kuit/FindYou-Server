package com.kuit.findyou.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProtectingReportInfoDTO {

    private List<String> imageUrls;
    private String breed;
    private String tag;
    private String age;
    private String weight;
    private String sex;
    private String happenDate;
    private String furColor;
    private String neutering;
    private String significant;
    private String noticeNumber;
    private String noticeDuration;
    private String foundLocation;
    private String careName;
    private String careAddr;
    private String careTel;
    private String authority;
    private String authorityPhoneNumber;
    private Boolean interest;

}
