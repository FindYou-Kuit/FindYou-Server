package com.kuit.findyou.domain.home.repository;

import com.kuit.findyou.domain.home.model.AnimalDepartment;
import com.kuit.findyou.domain.home.model.AnimalShelter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnimalDepartmentRepositoryTest {
    @Autowired
    private AnimalDepartmentRepository animalDepartmentRepository;
    @Autowired
    private EntityManager entityManager;
    @Test
    @Transactional
    void testSoftDelete(){
        // given
        AnimalDepartment department1 = AnimalDepartment.builder()
                .orgranization("서울특별시 송파구")
                .department("동물부")
                .phoneNumber("010-1111-1111")
                .build();
        AnimalDepartment department2 = AnimalDepartment.builder()
                .orgranization("서울특별시 송파구")
                .department("동물부")
                .phoneNumber("010-1111-1111")
                .build();
        AnimalDepartment savedDepartment1 = animalDepartmentRepository.save(department1);
        AnimalDepartment savedDepartment2 = animalDepartmentRepository.save(department2);

        // when
        animalDepartmentRepository.delete(savedDepartment1);
        entityManager.flush(); // soft delete는 트랜잭션이 끝나고 DB에 쿼리가 전달 때 실행되기에 flush

        Optional<AnimalDepartment> byId1 = animalDepartmentRepository.findById(savedDepartment1.getId());
        Optional<AnimalDepartment> byId2 = animalDepartmentRepository.findById(savedDepartment2.getId());

        //then
        assertThat(byId1.isPresent()).isEqualTo(false);
        assertThat(byId2.isPresent()).isEqualTo(true);
    }
}
