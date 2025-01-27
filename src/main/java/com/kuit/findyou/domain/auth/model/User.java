package com.kuit.findyou.domain.auth.model;

import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE users SET status = 'N' WHERE user_id = ?")
@SQLRestriction("status = 'Y'")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Lob
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    // 신고글에 대해 orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    // 최근 본 신고글 과의 양방향 연관 관계 설정
    // 최근 본 신고글에 대해 orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private List<ViewedReport> viewedReports = new ArrayList<>();

    // 관심 신고글 과의 양방향 연관 관계 설정
    // 관심 신고글에 대해 orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private List<InterestReport> interestReports = new ArrayList<>();

    // 최근 본 보호글 과의 양방향 연관 관계 설정
    // 최근 본 보호글에 대해 orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private List<ViewedProtectingReport> viewedProtectingReports = new ArrayList<>();

    // 관심 보호글 과의 양방향 연관 관계 설정
    // 관심 보호글에 대해 orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private List<InterestProtectingReport> interestProtectingReports = new ArrayList<>();


    // 연관 관계 편의 메서드들
    public void addReport(Report report) {
        this.reports.add(report);
    }

    public void addViewedReport(ViewedReport viewedReport) {
        viewedReports.add(viewedReport);
    }

    public void addInterestReport(InterestReport interestReport) {
        interestReports.add(interestReport);
    }

    public void addViewedProtectingReport(ViewedProtectingReport viewedProtectingReport) {
        viewedProtectingReports.add(viewedProtectingReport);
    }

    public void addInterestProtectingReport(InterestProtectingReport interestProtectingReport) {
        interestProtectingReports.add(interestProtectingReport);
    }


    public Boolean isInterestReport(Long reportId) {
        for(InterestReport interestReport : interestReports) {
            Report findReport = interestReport.getReport();
            if(findReport.getId().equals(reportId)) {
                return true;
            }
        }

        return false;
    }

    public Boolean isInterestProtectingReport(Long protectingReportId) {
        for (InterestProtectingReport interestProtectingReport : interestProtectingReports) {
            ProtectingReport findProtectingReport = interestProtectingReport.getProtectingReport();
            if (findProtectingReport.getId().equals(protectingReportId)) {
                return true;
            }
        }

        return false;
    }

    public void changeName(String newNickname) {
        this.name = newNickname;
    }
}
