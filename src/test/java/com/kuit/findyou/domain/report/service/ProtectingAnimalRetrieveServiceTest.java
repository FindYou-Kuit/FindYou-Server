package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ProtectingReportCardDTO;
import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ProtectingAnimalRetrieveServiceTest {

    @Autowired ProtectingReportRepository protectingReportRepository;
    @Autowired UserRepository userRepository;
    @Autowired InterestProtectingReportRepository interestProtectingReportRepository;
    @Autowired ProtectingAnimalRetrieveService protectingAnimalRetrieveService;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("1234567")
                .build();

        userRepository.save(user);


        for (int i = 1; i <= 41; i++) {
            ProtectingReport report = ProtectingReport.builder()
                    .happenDate(LocalDate.now())
                    .imageUrl(String.valueOf(i))
                    .species(String.valueOf(i))
                    .noticeNumber(String.valueOf(i))
                    .noticeStartDate(LocalDate.now())
                    .noticeEndDate(LocalDate.now())
                    .breed(String.valueOf(i))
                    .furColor(String.valueOf(i))
                    .weight(3.5F)
                    .age((short) i)
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
            protectingReportRepository.save(report);

            if(i > 4 && i < 15) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, report);
                interestProtectingReportRepository.save(interestProtectingReport);
            }

            if(i > 24 && i < 35) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, report);
                interestProtectingReportRepository.save(interestProtectingReport);
            }
        }
    }

    @Test
    void retrieveProtectingReport() {
        ProtectingReportCardDTO protectingReportCardDTO = protectingAnimalRetrieveService.retrieveProtectingReportCardsWithFilters(1L, 22L, null, null, null, null, null);

        List<Card> cards = protectingReportCardDTO.getCards();

        for(Card card : cards) {
            log.info("card : {} ", card);
        }

        log.info("lastProtectId : {}", protectingReportCardDTO.getLastProtectId());
        log.info("isLast : {}", protectingReportCardDTO.getIsLast());
    }


}