package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ReportCardDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ReportAnimalRetrieveServiceTest {

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ReportAnimalRepository reportAnimalRepository;
    @Autowired
    ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;
    @Autowired
    AnimalFeatureRepository animalFeatureRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestReportRepository interestReportRepository;
    @Autowired
    BreedRepository breedRepository;
    @Autowired
    ReportAnimalRetrieveService reportAnimalRetrieveService;
    @Autowired
    ImageRepository imageRepository;

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
        // 동물 특징 생성
        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);
        //=========================================

        for (int i = 1; i <= 41; i++) {
            //=========================================
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
            // 이미지 객체 생성
            List<Image> images = new ArrayList<>();
            images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
            images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

            images.forEach(imageRepository::save);
            //=========================================

            //=========================================
            // 신고글 작성
            String tag = "목격신고";
            if(i > 20) {
                tag = "실종신고";
            }

            Report report = Report.createReport(tag, String.valueOf(i), LocalDate.now(), String.valueOf(i), user, reportAnimal, images);
            reportRepository.save(report);
            //=========================================

            //=========================================
            // 관심 글로 등록
            if(i > 20) {
                InterestReport viewedReport = InterestReport.createInterestReport(user, report);
                interestReportRepository.save(viewedReport);
            }
            //=========================================
        }
    }

    @Test
    void retrieveReport() {
        ReportCardDTO reportCardDTO = reportAnimalRetrieveService.retrieveReportCardsWithFilters(1L, 2L, null, null, null, null, null);

        List<Card> cards = reportCardDTO.getCards();

        for(Card card : cards) {
            log.info("card : {} ", card);
        }

        log.info("lastProtectId : {}", reportCardDTO.getLastReportId());
        log.info("isLast : {}", reportCardDTO.getIsLast());
    }

}