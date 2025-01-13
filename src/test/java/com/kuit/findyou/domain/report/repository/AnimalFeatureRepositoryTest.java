package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.AnimalFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
class AnimalFeatureRepositoryTest {

    @Autowired AnimalFeatureRepository animalFeatureRepository;

    @Test
    void save() {
        AnimalFeature animalFeature = AnimalFeature.builder()
                        .featureValue("순함")
                                .build();
        animalFeatureRepository.save(animalFeature);
    }

}