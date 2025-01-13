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

import java.time.LocalDate;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE report SET status = 'N' WHERE report_id = ?")
@SQLRestriction("status = 'Y'")
public class Report extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long id;

    @Column(name = "tag", length = 50, nullable = false)
    private String tag;

    @Column(name = "found_location", length = 200, nullable = false)
    private String foundLocation;

    @Column(name = "event_date", nullable = false, columnDefinition = "DATE")
    private LocalDate eventDate;

    @Column(name = "additional_description")
    private String additionalDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_animal_id", nullable = false)
    private ReportAnimal reportAnimal;


    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static Report createReport(String tag, String foundLocation, LocalDate eventDate, String additionalDescription, User user, ReportAnimal reportAnimal) {
        Report report = new Report();
        report.tag = tag;
        report.foundLocation = foundLocation;
        report.eventDate = eventDate;
        report.additionalDescription = additionalDescription;
        report.setUser(user);
        report.reportAnimal = reportAnimal;
        return report;
    }

    private void setUser(User user) {
        this.user = user;
        user.addReport(this);
    }

}
