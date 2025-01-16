package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
//@Transactional
class ViewedProtectingReportRepositoryTest {

    @Autowired private ViewedProtectingReportRepository viewedProtectingReportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProtectingReportRepository protectingReportRepository;

    @Test
    void save() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
                .build();

        userRepository.save(user);

        ProtectingReport protectingReport = ProtectingReport.builder()
                .happenDate(LocalDate.now())
                .imageUrl("image.jpg")
                .species("개")
                .noticeNumber("12345123")
                .noticeStartDate(LocalDate.now())
                .noticeEndDate(LocalDate.now())
                .breed("시츄")
                .furColor("갈색")
                .weight(3.5F)
                .age((short)2024)
                .sex(Sex.M)
                .neutering(Neutering.N)
                .foundLocation("우리집 앞")
                .significant("눈이 아파보임")
                .careName("행운사")
                .careAddr("용산구")
                .careTel("02-1234-1234")
                .authority("관할서")
                .authorityPhoneNumber("02-111-4312")
                .build();

        protectingReportRepository.save(protectingReport);

        ViewedProtectingReport viewedProtectingReport = ViewedProtectingReport.createViewedProtectingReport(user, protectingReport);
        viewedProtectingReportRepository.save(viewedProtectingReport);
    }

    @Test
    void findByUserAndProtectingReport() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
                .build();

        userRepository.save(user);

        ProtectingReport protectingReport = ProtectingReport.builder()
                .happenDate(LocalDate.now())
                .imageUrl("image.jpg")
                .species("개")
                .noticeNumber("12345123")
                .noticeStartDate(LocalDate.now())
                .noticeEndDate(LocalDate.now())
                .breed("시츄")
                .furColor("갈색")
                .weight(3.5F)
                .age((short)2024)
                .sex(Sex.M)
                .neutering(Neutering.N)
                .foundLocation("우리집 앞")
                .significant("눈이 아파보임")
                .careName("행운사")
                .careAddr("용산구")
                .careTel("02-1234-1234")
                .authority("관할서")
                .authorityPhoneNumber("02-111-4312")
                .build();

        protectingReportRepository.save(protectingReport);

        ViewedProtectingReport viewedProtectingReport = ViewedProtectingReport.createViewedProtectingReport(user, protectingReport);
        viewedProtectingReportRepository.save(viewedProtectingReport);

        Optional<ViewedProtectingReport> foundReport = viewedProtectingReportRepository.findByUserAndProtectingReport(user, protectingReport);

        Assertions.assertTrue(foundReport.isPresent());
        Assertions.assertEquals(viewedProtectingReport.getId(), foundReport.get().getId());
    }

}