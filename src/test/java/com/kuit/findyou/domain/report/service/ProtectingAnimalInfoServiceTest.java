package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProtectingAnimalInfoServiceTest {

    @Autowired ProtectingAnimalInfoService protectingAnimalInfoService;
    @Autowired ProtectingReportRepository protectingReportRepository;
    @Autowired UserRepository userRepository;
    @Autowired InterestProtectingReportRepository interestProtectingReportRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
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

        ProtectingReportInfoDTO protectingReportInfo = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, userId);

        assertThat(protectingReportInfo.getSex()).isEqualTo("수컷");
        assertThat(protectingReportInfo.getNeutering()).isEqualTo("아니요");
        assertThat(protectingReportInfo.getInterest()).isFalse();


    }

}