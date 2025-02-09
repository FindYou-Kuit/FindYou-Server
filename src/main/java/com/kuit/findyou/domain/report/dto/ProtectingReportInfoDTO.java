package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProtectingReportInfoDTO {

    @Schema(description = "이미지 url들")
    private String imageUrl;

    @Schema(description = "품종 정보", example = "말티즈")
    private String breed;

    @Schema(description = "태그", example = "보호중")
    private String tag;

    @Schema(description = "나이", example = "2024, 1살")
    private String age;

    @Schema(description = "몸무게", example = "2kg")
    private String weight;

    @Schema(description = "성별", example = "수컷")
    private String sex;

    @Schema(description = "게시 날짜", example = "2024-11-23")
    private String happenDate;

    @Schema(description = "털색", example = "갈색, 흰색")
    private String furColor;

    @Schema(description = "중성화", example = "아니요")
    private String neutering;

    @Schema(description = "특이사항", example = "다리가 불편함")
    private String significant;

    @Schema(description = "공고 번호", example = "경남-창원1-2024-00545")
    private String noticeNumber;

    @Schema(description = "공고 기간", example = "2024-11-23 ~ 2024-12-03")
    private String noticeDuration;

    @Schema(description = "구조 장소", example = "성산구 내동")
    private String foundLocation;

    @Schema(description = "보호소 이름", example = "태민동물병원")
    private String careName;

    @Schema(description = "보호소 주소", example = "서울특별시 광진구 ...")
    private String careAddr;

    @Schema(description = "보호소 전화번호", example = "010-2345-6789")
    private String careTel;

    @Schema(description = "관할 기관", example = "창원유기동물보호소")
    private String authority;

    @Schema(description = "관할 기관 전화번호", example = "055-930-3562")
    private String authorityPhoneNumber;

    @Schema(description = "관심 보호글 여부")
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
