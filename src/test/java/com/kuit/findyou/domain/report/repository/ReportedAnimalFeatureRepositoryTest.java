package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.AnimalFeature;
import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.model.ReportedAnimalFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReportedAnimalFeatureRepositoryTest {

    @Autowired
    private ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;
    @Autowired
    private AnimalFeatureRepository animalFeatureRepository;
    @Autowired
    private BreedRepository breedRepository;
    @Autowired
    private ReportAnimalRepository reportAnimalRepository;

    @Test
    void save() {
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);

        Breed findBreed = breedRepository.findById(breed.getId()).get();

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(findBreed)
                .build();
        reportAnimalRepository.save(reportAnimal);

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature reportedAnimalFeature2 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        reportedAnimalFeatureRepository.save(reportedAnimalFeature);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature2);

        ReportAnimal findAnimal = reportAnimalRepository.findById(reportAnimal.getId()).get();
        for(ReportedAnimalFeature features : findAnimal.getReportedAnimalFeatures()) {
            System.out.println(features.getFeature().getFeatureValue());
        }


    }


}