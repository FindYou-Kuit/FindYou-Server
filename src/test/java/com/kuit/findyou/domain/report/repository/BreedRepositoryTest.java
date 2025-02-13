package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.breed.model.Breed;
import com.kuit.findyou.domain.breed.repository.BreedRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BreedRepositoryTest {

    @Autowired private BreedRepository breedRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void save() {
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);

        em.flush();
        em.clear();

        Breed findBreed = breedRepository.findById(1L).get();
        System.out.println(findBreed.getStatus());
    }

}