package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ReportInfoDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
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

    @BeforeEach
    void setUp() {

        //=========================================
        // 유저 설정
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
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
        reportAnimalRepository.save(reportAnimal);
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
        ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature reportedAnimalFeature2 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature2);

        //=========================================
        // 신고글 작성
        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal);
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
        Long reportId = 21L;
        Long userId = 1L;

        User findUser = userRepository.findById(userId).get();

        ReportInfoDTO reportInfo = reportAnimalInfoService.findReportInfoById(reportId, userId);

        ReportInfoDTO reportInfo2 = reportAnimalInfoService.findReportInfoById(reportId, userId);

//        Assertions.assertThat(reportInfo.getInterest()).isTrue();

        Assertions.assertThat(findUser.getViewedReports()).size().isEqualTo(1);
    }

}