package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ViewedCardDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Slf4j
@Rollback(value = false)
class ViewedAnimalRetrieveServiceTest {

    @Autowired
    ViewedAnimalRetrieveService viewedAnimalRetrieveService;

    @Autowired
    ProtectingReportRepository protectingReportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ViewedProtectingReportRepository viewedProtectingReportRepository;
    @Autowired
    ViewedReportRepository viewedReportRepository;
    @Autowired
    BreedRepository breedRepository;
    @Autowired
    AnimalFeatureRepository animalFeatureRepository;

    @Autowired InterestProtectingReportRepository interestProtectingReportRepository;
    @Autowired InterestReportRepository interestReportRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("1234567")
                .build();

        userRepository.save(user);

        //=========================================
        // 품종, 축종 설정
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);
        //=========================================

        //=========================================
        // 동물 특징 생성
        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);
        //=========================================


        for (int i = 1; i <= 41; i++) {
            ProtectingReport protectingReport = ProtectingReport.builder()
                    .happenDate(LocalDate.now())
                    .imageUrl(String.valueOf(i))
                    .species(String.valueOf(i))
                    .noticeNumber(String.valueOf(i))
                    .noticeStartDate(LocalDate.now())
                    .noticeEndDate(LocalDate.now())
                    .breed(String.valueOf(i))
                    .furColor(String.valueOf(i))
                    .weight(String.valueOf(3.5F))
                    .age(String.valueOf(i))
                    .sex(Sex.M)
                    .neutering(Neutering.N)
                    .foundLocation(String.valueOf(i))
                    .significant(String.valueOf(i))
                    .careName(String.valueOf(i))
                    .careAddr(String.valueOf(i))
                    .careTel(String.valueOf(i))
                    .authority(String.valueOf(i))
                    .authorityPhoneNumber(String.valueOf(i))
                    .build();
            protectingReportRepository.save(protectingReport);

            if (i > 4 && i < 15) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
                interestProtectingReportRepository.save(interestProtectingReport);


            }

            if (i > 24 && i < 35) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
                interestProtectingReportRepository.save(interestProtectingReport);
            }

            ViewedProtectingReport viewedProtectingReport = ViewedProtectingReport.createViewedProtectingReport(user, protectingReport);
            viewedProtectingReportRepository.save(viewedProtectingReport);
        }

        for (int i = 1; i <= 67; i++) {
            // 신고 동물 설정
            ReportAnimal reportAnimal = ReportAnimal.builder()
                    .furColor(String.valueOf(i))
                    .breed(breed)
                    .build();
            //=========================================


            //=========================================
            // 신고 동물에 특징 매핑
            ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
            ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

            //=========================================
            //이미지 객체 생성
            Image image1 = Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString());
            Image image2 = Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString());

            List<Image> images = new ArrayList<>();
            images.add(image1);
            images.add(image2);
            //=========================================

            //=========================================
            // 신고글 작성
            String tag = "목격신고";
            if (i > 20) {
                tag = "실종신고";
            }

            Report report = Report.createReport(tag, String.valueOf(i), LocalDate.now(), String.valueOf(i), user, reportAnimal, images);
            reportRepository.save(report);
            //=========================================

            //=========================================
            // 관심 글로 등록
            if (i > 20) {
                InterestReport viewedReport = InterestReport.createInterestReport(user, report);
                interestReportRepository.save(viewedReport);
            }
            //=========================================

            ViewedReport viewedReport = ViewedReport.createViewedReport(user, report);
            viewedReportRepository.save(viewedReport);
        }
    }

    @Test
    @DisplayName("최근 본 글 조회 테스트")
    void getViewedReports() {
        ViewedCardDTO viewedCardDTO = viewedAnimalRetrieveService.retrieveAllViewedReports(1L, 1L, 4L);

        List<Card> viewedAnimals = viewedCardDTO.getViewedAnimals();

        for(Card card : viewedAnimals) {
            log.info("card : {} ", card);
        }

        log.info("lastProtectId : {}", viewedCardDTO.getLastViewedProtectId());
        log.info("lastReportId : {}", viewedCardDTO.getLastViewedReportId());
        log.info("isLast : {}", viewedCardDTO.getIsLast());
    }
}

