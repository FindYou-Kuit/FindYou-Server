package com.kuit.findyou.report.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "animal_feature")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE animal_feature SET status = 'N' WHERE animal_feature_id = ?")
@SQLRestriction("status = 'Y'")
public class AnimalFeature extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_feature_id", nullable = false)
    private Long id;

    @Column(name = "feature_value", length = 50, nullable = false)
    private String featureValue;
}
