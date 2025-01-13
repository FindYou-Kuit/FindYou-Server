package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReportAnimalRepositoryTest {

    @Autowired
    private ReportAnimalRepository reportAnimalRepository;
    @Autowired
    private BreedRepository breedRepository;

    @Test
    void save() {
        Breed breed = Breed.builder()
                .name("치와와")
                .species("개")
                .build();

        breedRepository.save(breed);

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();

        reportAnimalRepository.save(reportAnimal);
    }

}