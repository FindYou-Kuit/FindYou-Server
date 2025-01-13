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
@Table(name = "viewed_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE viewed_report SET status = 'N' WHERE viewed_report_id = ?")
@SQLRestriction("status = 'Y'")
public class ViewedReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "viewed_report_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static ViewedReport createViewedReport(User user, Report report) {
        ViewedReport viewedReport = new ViewedReport();
        viewedReport.setUser(user);
        viewedReport.report = report;
        return viewedReport;
    }

    // 연관 관계 편의 메서드
    private void setUser(User user) {
        this.user = user;
        user.addViewedReport(this);
    }
}
