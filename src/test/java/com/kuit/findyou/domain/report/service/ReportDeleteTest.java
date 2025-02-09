package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.image.model.Image;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.AnimalFeatureRepository;
import com.kuit.findyou.domain.report.repository.BreedRepository;
import com.kuit.findyou.domain.report.repository.ReportAnimalRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReportDeleteTest {

    @Autowired private ReportDeleteService reportDeleteService;
    @Autowired private ReportRepository reportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BreedRepository breedRepository;
    @Autowired private ReportAnimalRepository animalRepository;
    @Autowired private AnimalFeatureRepository animalFeatureRepository;

    private Report report;
    private User user;
    private Breed breed;
    private ReportAnimal reportAnimal;

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

        reportAnimal = ReportAnimal.builder()
                .furColor("흰색")
                .sex("M")
                .breed(breed)
                .build();

        AnimalFeature feature1 = AnimalFeature.builder()
                .featureValue("1")
                .build();
        animalFeatureRepository.save(feature1);

        AnimalFeature feature2 = AnimalFeature.builder()
                .featureValue("2")
                .build();
        animalFeatureRepository.save(feature2);

        List<AnimalFeature> features = new ArrayList<>();
        features.add(feature1);
        features.add(feature2);

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색")
                .sex("M")
                .breed(breed)
                .build();

        features.forEach(feature -> ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, feature));

        List<Image> images = new ArrayList<>();


        report = Report.createReport(
                ReportTag.MISSING,
                "서울",
                LocalDate.now(),
                "상세 설명",
                user,
                reportAnimal,
                images
        );

        reportRepository.save(report);

        report = reportRepository.save(report);
    }

    @Test
    void deleteReport_Success() {
        reportDeleteService.deleteReport(report.getId());

        // 삭제 후 데이터베이스에서 해당 객체가 없는지 확인
        assertThat(reportRepository.findById(report.getId())).isEmpty();

    }

}
