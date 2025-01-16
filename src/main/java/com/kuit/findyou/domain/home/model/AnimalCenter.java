package com.kuit.findyou.domain.home.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "animal_center")
@SQLDelete(sql = "UPDATE animal_center SET status = 'N' WHERE id = ?")
@SQLRestriction("status = 'Y'")
public class AnimalCenter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String juristiction;

    @Column(length = 70, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @Column(length = 255, nullable = false)
    private String address;
}
