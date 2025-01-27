package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ProtectingAnimalInfoServiceTest {

    @Autowired ProtectingAnimalInfoService protectingAnimalInfoService;
    @Autowired ProtectingReportRepository protectingReportRepository;
    @Autowired UserRepository userRepository;
    @Autowired InterestProtectingReportRepository interestProtectingReportRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("1234567")
                .build();

        userRepository.save(user);


        for (int i = 1; i <= 10; i++) {
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

            if(i > 4) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, report);
                interestProtectingReportRepository.save(interestProtectingReport);
            }
        }
    }

    @Test
    void findProtectingReportInfoById() {
        Long userId = 1L;
        Long protectingReportId = 6L;

        User findUser = userRepository.findById(userId).get();

        ProtectingReportInfoDTO protectingReportInfo = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, userId);

        ProtectingReportInfoDTO protectingReportInfo2 = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, userId);

        assertThat(protectingReportInfo.getSex()).isEqualTo("수컷");
        assertThat(protectingReportInfo.getNeutering()).isEqualTo("아니요");
        assertThat(protectingReportInfo.getInterest()).isTrue();

        em.flush();
        em.clear();

        User findUser2 = userRepository.findById(userId).get();

        assertThat(findUser2.getViewedProtectingReports()).size().isEqualTo(1);

        for (ViewedProtectingReport protectingReport : findUser2.getViewedProtectingReports()) {
            log.info("protectingReport.imageUrl = {}", protectingReport.getProtectingReport().getImageUrl());
        }


    }

}