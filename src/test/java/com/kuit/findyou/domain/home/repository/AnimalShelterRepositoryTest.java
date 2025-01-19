package com.kuit.findyou.domain.home.repository;

import com.kuit.findyou.domain.home.model.AnimalShelter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnimalShelterRepositoryTest {
    @Autowired
    private AnimalShelterRepository animalShelterRepository;

    @Autowired
    private EntityManager entityManager;
    @Test
    @Transactional
    void testSoftDelete(){
        // given
        AnimalShelter shelter = AnimalShelter.builder()
                .location("서울특별시 송파구")
                .name("동물사랑보호소")
                .phoneNumber("010-1111-1111")
                .build();

        AnimalShelter savedShelter = animalShelterRepository.save(shelter);

        // when
        animalShelterRepository.delete(savedShelter);
        entityManager.flush(); // soft delete는 트랜잭션이 끝나고 DB에 쿼리가 전달 때 실행되기에 flush

        Optional<AnimalShelter> byId = animalShelterRepository.findById(savedShelter.getId());

        //then
        assertThat(byId.isPresent()).isEqualTo(false);
    }
}
