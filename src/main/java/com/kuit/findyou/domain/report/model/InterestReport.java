package com.kuit.findyou.domain.report.model;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "interest_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("status = 'Y'")
public class InterestReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_report_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static InterestReport createInterestReport(User user, Report report) {
        InterestReport interestReport = new InterestReport();
        interestReport.setUser(user);
        interestReport.setReport(report); // 연관 관계 편의 메서드 적용
        return interestReport;
    }

    // User 에 대한 연관 관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.addInterestReport(this);
    }

    // Report 에 대한 연관 관계 편의 메서드
    private void setReport(Report report) {
        this.report = report;
        report.addInterestReport(this);
    }
}
