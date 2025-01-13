package com.kuit.findyou.report.model;

import com.kuit.findyou.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "breed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE breed SET status = 'N' WHERE breed_id = ?")
@SQLRestriction("status = 'Y'")
public class Breed extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_id", nullable = false)
    private Long id;

    @Column(name = "breed_name", length = 100, nullable = false)
    private String name;

    @Column(name = "species", length = 20, nullable = false)
    private String species;

    // [개]웰시코기 와 같이 축종 + 품종을 반환하는 메서드
    public String getSpeciesAndBreed() {
        return "[" + species + "]" + name;
    }
}
