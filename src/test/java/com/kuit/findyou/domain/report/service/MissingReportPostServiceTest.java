package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.FindyouApplication;
import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.MissingReportDTO;
import com.kuit.findyou.domain.report.exception.ReportCreationException;
import com.kuit.findyou.domain.report.model.AnimalFeature;
import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.model.Image;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.AnimalFeatureRepository;
import com.kuit.findyou.domain.report.repository.BreedRepository;
import com.kuit.findyou.domain.report.repository.ImageRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class MissingReportPostServiceTest {

    @Autowired private MissingReportPostService missingReportPostService;
    @Autowired private ReportRepository reportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BreedRepository breedRepository;
    @Autowired private AnimalFeatureRepository animalFeatureRepository;
    @Autowired private ImageRepository imageRepository;

    @PersistenceContext
    private EntityManager em;

    private User user;
    private Breed breed;
    private AnimalFeature feature1, feature2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("정주연")
                .email("jjuyaa@naver.com")
                .password("123123")
                .build();
        userRepository.save(user);

        breed = Breed.builder()
                .name("말티즈")
                .species("개")
                .build();
        breedRepository.save(breed);

        feature1 = AnimalFeature.builder()
                .featureValue("A1")//특징-스트링 아닌 식별ID 지정 예정
                .build();
        feature2 = AnimalFeature.builder()
                .featureValue("B2")
                .build();
        animalFeatureRepository.saveAll(Arrays.asList(feature1, feature2));
    }

    @Test
    void createReport_success() {
        imageRepository.save(Image.createImage("s3://findyoubucket/0066a3a2-85de-46fb-8ea8-501cfcf74074.jpg", "0066a3a2-85de-46fb-8ea8-501cfcf740740066a3a2-85de-46fb-8ea8-501cfcf74074"));
        imageRepository.save(Image.createImage("s3://findyoubucket/062d2851-0294-4e3a-b87b-3952a783bacd", "062d2851-0294-4e3a-b87b-3952a783bacd"));

        User findUser = userRepository.findById(1L).get();
        Breed findBreed = breedRepository.findById(1L).get();

        AnimalFeature animalFeature1 = animalFeatureRepository.findById(1L).get();
        AnimalFeature animalFeature2 = animalFeatureRepository.findById(2L).get();

        MissingReportDTO dto = MissingReportDTO.builder()
                .userId(user.getId())
                .breed(breed.getId())
                .sex("M")
                .furColor(Arrays.asList("갈색"))
                .location("서울")
                .features(Arrays.asList(feature1.getId(), feature2.getId()))
                .description("잃어버린 강아지를 찾습니다.")
                .missingDate(LocalDate.now())
                .imageUrls(Arrays.asList("s3://findyoubucket/0066a3a2-85de-46fb-8ea8-501cfcf74074.jpg", "s3://findyoubucket/062d2851-0294-4e3a-b87b-3952a783bacd"))
                .build();

        missingReportPostService.createReport(dto);

        em.flush();
        //em.clear();

        List<Report> reports = reportRepository.findAll();
        assertThat(reports).isNotEmpty();
        assertThat(reports.get(0).getUser()).isEqualTo(findUser);
        assertThat(reports.get(0).getReportAnimal().getBreed()).isEqualTo(findBreed);
        assertThat(reports.get(0).getImages()).hasSize(2);


        List<Long> savedFeatureIds = reports.get(0).getReportAnimal().getReportedAnimalFeatures()
                .stream()
                .map(raf -> raf.getFeature().getId())
                .collect(Collectors.toList());
        assertThat(savedFeatureIds).containsExactlyInAnyOrder(animalFeature1.getId(), animalFeature2.getId());
    }
}

