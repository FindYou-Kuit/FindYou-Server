package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.image.model.Image;
import com.kuit.findyou.domain.image.repository.ImageRepository;
import com.kuit.findyou.domain.report.dto.ReportInfoDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Slf4j
class ReportAnimalInfoServiceTest {

    // Report, ReportAnimal, ReportedAnimalFeature, AnimalFeature, User

    @Autowired ReportAnimalInfoService reportAnimalInfoService;
    @Autowired ReportRepository reportRepository;
    @Autowired ReportAnimalRepository reportAnimalRepository;
    @Autowired ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;
    @Autowired AnimalFeatureRepository animalFeatureRepository;
    @Autowired UserRepository userRepository;
    @Autowired InterestReportRepository interestReportRepository;
    @Autowired BreedRepository breedRepository;
    @Autowired
    ImageRepository imageRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setUp() {

        //=========================================
        // 유저 설정
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("1234567")
                .build();

        userRepository.save(user);
        //=========================================

        //=========================================
        // 품종, 축종 설정
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);
        //=========================================

        //=========================================
        // 신고 동물 설정
        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();
        //=========================================

        //=========================================
        // 동물 특징 생성
        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);
        //=========================================

        //=========================================
        // 신고 동물에 특징 매핑
        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        //=========================================
        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

        images.forEach(imageRepository::save);
        //=========================================

        //=========================================
        // 신고글 작성
        Report report = Report.createReport(ReportTag.WITNESSED, "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);
        //=========================================

        //=========================================
        // 관심 글로 등록
        InterestReport interestReport = InterestReport.createInterestReport(user, report);
        interestReportRepository.save(interestReport);
        //=========================================
    }

    @Test
    void findReportInfoById() {
        Long reportId = 1L;
        Long userId = 1L;

        User findUser = userRepository.findById(userId).get();

        ReportInfoDTO reportInfo = reportAnimalInfoService.findReportInfoById(reportId, userId);

        ReportInfoDTO reportInfo2 = reportAnimalInfoService.findReportInfoById(reportId, userId);

//        Assertions.assertThat(reportInfo.getInterest()).isTrue();

        em.flush();
        em.clear();

        User findUser2 = userRepository.findById(userId).get();

        Assertions.assertThat(findUser2.getViewedReports()).size().isEqualTo(1);

        for (String imageUrl : reportInfo.getImageUrls()) {
            log.info("imageUrl : {}", imageUrl);
        }
    }

}