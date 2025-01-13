package com.kuit.findyou.report.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_animal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE report_animal SET status = 'N' WHERE report_animal_id = ?")
@SQLRestriction("status = 'Y'")
public class ReportAnimal extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_animal_id", nullable = false)
    private Long id;

    @Column(name = "fur_color", length = 50, nullable = false)
    private String furColor;

    @Column(length = 10)
    private String sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breed_id", nullable = false)
    private Breed breed;

    // 신고 동물의 특징을 알아오기 위한 양방향 연관관계 설정
    @OneToMany(mappedBy = "reportAnimal")
    @Builder.Default
    private List<ReportedAnimalFeature> reportedAnimalFeatures = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addReportedAnimalFeature(ReportedAnimalFeature reportedAnimalFeature) {
        reportedAnimalFeatures.add(reportedAnimalFeature);
    }


}
