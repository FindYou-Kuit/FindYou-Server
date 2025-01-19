package com.kuit.findyou.domain.home.repository;

import com.kuit.findyou.domain.home.model.AnimalCenter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnimalCenterRepositoryTest {
    @Autowired
    private AnimalCenterRepository animalCenterRepository;

    @Autowired
    private EntityManager entityManager;
    @Test
    @Transactional
    void testSoftDelete(){
        // given
        AnimalCenter center1 = AnimalCenter.builder()
                .juristiction("대구광역시 수성구")
                .name("사)대구수의사회")
                .phoneNumber("053-764-3708")
                .address("대구광역시 북구 호국로 229 (서변동) 6층")
                .build();

        AnimalCenter savedCenter = animalCenterRepository.save(center1);

        // when
        animalCenterRepository.delete(savedCenter);
        entityManager.flush(); // soft delete는 트랜잭션이 끝나고 DB에 쿼리가 전달 때 실행되기에 flush

        Optional<AnimalCenter> byId = animalCenterRepository.findById(savedCenter.getId());

        //then
        assertThat(byId.isPresent()).isEqualTo(false);
    }
}
