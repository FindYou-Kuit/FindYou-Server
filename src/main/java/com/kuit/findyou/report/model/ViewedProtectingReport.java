package com.kuit.findyou.report.model;

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
@Table(name = "viewed_protecting_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE viewed_protecting_report SET status = 'N' WHERE viewed_protecting_report_id = ?")
@SQLRestriction("status = 'Y'")
public class ViewedProtectingReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "viewed_protecting_report_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protecting_report_id", nullable = false)
    private ProtectingReport protectingReport;

    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static ViewedProtectingReport createViewedProtectingReport(User user, ProtectingReport protectingReport) {
        ViewedProtectingReport viewedProtectingReport = new ViewedProtectingReport();
        viewedProtectingReport.setUser(user);
        viewedProtectingReport.protectingReport = protectingReport;
        return viewedProtectingReport;
    }

    // 연관 관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.addViewedProtectingReport(this);
    }
}
