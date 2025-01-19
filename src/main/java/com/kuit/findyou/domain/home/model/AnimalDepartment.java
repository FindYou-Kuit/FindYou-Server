package com.kuit.findyou.domain.home.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "animal_department")
@SQLDelete(sql = "UPDATE animal_department SET status = 'N' WHERE id = ?")
@SQLRestriction("status = 'Y'")
public class AnimalDepartment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100, nullable = false)
    private String orgranization;

    @Column(length=70, nullable = false)
    private String department;

    @Column(length=20, nullable = false)
    private String phoneNumber;
}
