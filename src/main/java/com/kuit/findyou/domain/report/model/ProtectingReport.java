package com.kuit.findyou.domain.report.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "protecting_report")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE protecting_report SET status = 'N' WHERE protecting_report_id = ?")
@SQLRestriction("status = 'Y'")
public class ProtectingReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "protecting_report_id", nullable = false)
    private Long id;

    @Column(name = "happen_date", columnDefinition = "DATE", nullable = false)
    private LocalDate happenDate;

    @Lob
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "species", length = 20, nullable = false)
    private String species;

    @Column(name = "notice_number", length = 30, nullable = false)
    private String noticeNumber;

    @Column(name = "notice_start_date", columnDefinition = "DATE", nullable = false)
    private LocalDate noticeStartDate;

    @Column(name = "notice_end_date", columnDefinition = "DATE", nullable = false)
    private LocalDate noticeEndDate;

    @Column(name = "breed", length = 100, nullable = false)
    private String breed;

    @Column(name = "fur_color", length = 30, nullable = false)
    private String furColor;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "age", nullable = false)
    private Short age;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1)", nullable = false)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1)", nullable = false)
    private Neutering neutering;

    @Column(name = "found_location", length = 100, nullable = false)
    private String foundLocation;

    @Column(name = "significant", length = 200, nullable = false)
    private String significant;

    @Column(name = "care_name", length = 50, nullable = false)
    private String careName;

    @Column(name = "care_addr", length = 200, nullable = false)
    private String careAddr;

    @Column(name = "care_tel", length = 14, nullable = false)
    private String careTel;

    @Column(name = "authority", length = 50, nullable = false)
    private String authority;

    @Column(name = "authority_phone_number", length = 14, nullable = false)
    private String authorityPhoneNumber;

    // 최근 본 보호글 삭제를 위한 양방향 연관관계 설정
    // orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "protectingReport", orphanRemoval = true)
    @Builder.Default
    private List<ViewedProtectingReport> viewedProtectingReports = new ArrayList<>();

    // 관심 보호글 삭제를 위한 양방향 연관관계 설정
    // orphanRemoval = true 만 설정
    @OneToMany(mappedBy = "protectingReport", orphanRemoval = true)
    @Builder.Default
    private List<InterestProtectingReport> interestProtectingReports = new ArrayList<>();


    public void addViewedProtectingReport(ViewedProtectingReport viewedProtectingReport) {
        viewedProtectingReports.add(viewedProtectingReport);
    }

    public void addInterestProtectingReport(InterestProtectingReport interestProtectingReport) {
        interestProtectingReports.add(interestProtectingReport);
    }


    public String getNoticeDuration() {
        return noticeStartDate + " ~ " + noticeEndDate;
    }

    public String getAnimalSex() {
        if (sex.equals(Sex.M)) {
            return "수컷";
        } else if (sex.equals(Sex.F)) {
            return "암컷";
        } else {
            return "미상";
        }
    }

    public String getAnimalNeutering() {
        if (neutering.equals(Neutering.Y)) {
            return "예";
        } else if (neutering.equals(Neutering.N)) {
            return "아니요";
        } else {
            return "미상";
        }
    }

    public String getWeightWithKg() {
        return weight + "kg";
    }

    public String getAgeWithYear() {
        int age = LocalDate.now().getYear() - this.age + 1;
        return this.age + ", " + age + "살";
    }
}
