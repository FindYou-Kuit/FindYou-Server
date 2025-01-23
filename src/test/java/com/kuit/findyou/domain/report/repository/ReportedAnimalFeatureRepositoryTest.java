package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.AnimalFeature;
import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.model.ReportedAnimalFeature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReportedAnimalFeatureRepositoryTest {

    @Autowired
    private AnimalFeatureRepository animalFeatureRepository;
    @Autowired
    private BreedRepository breedRepository;
    @Autowired
    private ReportAnimalRepository reportAnimalRepository;
    @Autowired
    private ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;

    @BeforeEach
    void setUp() {
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);

        Breed findBreed = breedRepository.findById(breed.getId()).get();

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(findBreed)
                .build();

        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        reportAnimalRepository.save(reportAnimal);
    }


    @Test
    void save() {
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);

        Breed findBreed = breedRepository.findById(breed.getId()).get();

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(findBreed)
                .build();

        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        reportAnimalRepository.save(reportAnimal);

        // ReportedAnimalFeature 를 명시적으로 save 해주지 않아도 reportAnimal을 save하는 순간 DB에 자동으로 데이터가 삽입됨

//        ReportAnimal findAnimal = reportAnimalRepository.findById(reportAnimal.getId()).get();
//        for(ReportedAnimalFeature features : findAnimal.getReportedAnimalFeatures()) {
//            System.out.println(features.getFeature().getFeatureValue());
//        }


    }

    @Test
    @DisplayName("신고 동물 삭제시 신고 동물 특징 삭제 여부 확인")
    void delete() {
        ReportAnimal reportAnimal = reportAnimalRepository.findById(1L).get();

        reportAnimalRepository.delete(reportAnimal);

        Assertions.assertThat(reportedAnimalFeatureRepository.findAll()).isEmpty();
    }


}