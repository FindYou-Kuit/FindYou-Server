package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProtectingReportInfoDTO {

    private String imageUrl;
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


    public static ProtectingReportInfoDTO newInstanceFromProtectingReportWithUser(ProtectingReport protectingReport, User loginedUser) {
        return ProtectingReportInfoDTO.builder()
                .imageUrl(protectingReport.getImageUrl())
                .breed(protectingReport.getBreed())
                .tag(ReportTag.PROTECTING.getValue())
                .age(protectingReport.getAgeWithYear())
                .weight(protectingReport.getWeightWithKg())
                .sex(protectingReport.getAnimalSex())
                .happenDate(protectingReport.getHappenDate().toString())
                .furColor(protectingReport.getFurColor())
                .neutering(protectingReport.getAnimalNeutering())
                .significant(protectingReport.getSignificant())
                .noticeNumber(protectingReport.getNoticeNumber())
                .noticeDuration(protectingReport.getNoticeDuration())
                .foundLocation(protectingReport.getFoundLocation())
                .careName(protectingReport.getCareName())
                .careAddr(protectingReport.getCareAddr())
                .careTel(protectingReport.getCareTel())
                .authority(protectingReport.getAuthority())
                .authorityPhoneNumber(protectingReport.getAuthorityPhoneNumber())
                .interest(loginedUser.isInterestProtectingReport(protectingReport.getId()))
                .build();
    }
}
