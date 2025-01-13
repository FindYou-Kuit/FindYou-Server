package com.kuit.findyou.domain.report.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "report_animal_feature")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE report_animal_feature SET status = 'N' WHERE report_animal_feature_id = ?")
@SQLRestriction("status = 'Y'")
public class ReportedAnimalFeature extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "report_animal_feature_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_animal_id", nullable = false)
    private ReportAnimal reportAnimal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_feature_id", nullable = false)
    private AnimalFeature feature;

    //==생성 메서드==// -> 생성자 말고 생성 메서드를 별도로 만든 형태
    public static ReportedAnimalFeature createReportedAnimalFeature(ReportAnimal reportAnimal, AnimalFeature feature) {
        ReportedAnimalFeature reportedAnimalFeature = new ReportedAnimalFeature();
        reportedAnimalFeature.setReportAnimal(reportAnimal);
        reportedAnimalFeature.feature = feature;
        return reportedAnimalFeature;
    }

    // 연관 관계 편의 메서드
    private void setReportAnimal(ReportAnimal reportAnimal) {
        this.reportAnimal = reportAnimal;
        reportAnimal.addReportedAnimalFeature(this);
    }



}
