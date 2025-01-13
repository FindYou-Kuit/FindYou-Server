package com.kuit.findyou.domain.report.model;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "interest_protecting_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE interest_protecting_report SET status = 'N' WHERE interest_protecting_id = ?")
@SQLRestriction("status = 'Y'")
public class InterestProtectingReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_protecting_report_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protecting_report_id", nullable = false)
    private ProtectingReport protectingReport;

    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static InterestProtectingReport createInterestProtectingReport(User user, ProtectingReport protectingReport) {
        InterestProtectingReport interestProtectingReport = new InterestProtectingReport();
        interestProtectingReport.setUser(user);
        interestProtectingReport.protectingReport = protectingReport;
        return interestProtectingReport;
    }

    // 연관 관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.addInterestProtectingReport(this);
    }


}
