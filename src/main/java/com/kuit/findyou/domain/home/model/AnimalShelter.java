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
@Table(name = "animal_shelter")
@SQLDelete(sql = "UPDATE animal_shelter SET status = 'N' WHERE id = ?")
@SQLRestriction("status = 'Y'")
public class AnimalShelter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String location;

    @Column(length = 70, nullable = false)
    private String name;

    @Column(length = 20)
    private String phoneNumber;
}
