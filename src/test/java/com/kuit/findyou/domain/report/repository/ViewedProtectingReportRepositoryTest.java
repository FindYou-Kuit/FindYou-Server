package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
class ViewedProtectingReportRepositoryTest {

    @Autowired private ViewedProtectingReportRepository viewedProtectingReportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProtectingReportRepository protectingReportRepository;

    @BeforeEach
    void setUp() {
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
                .weight(String.valueOf(3.5F))
                .age(String.valueOf((short)2024))
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
                .weight(String.valueOf(3.5F))
                .age(String.valueOf((short)2024))
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
    void delete() {
        ViewedProtectingReport viewedProtectingReport = viewedProtectingReportRepository.findById(1L).get();
        viewedProtectingReportRepository.delete(viewedProtectingReport);
    }

    @Test
    @DisplayName("유저 삭제시 최근 본 보호글 삭제 여부 확인")
    void UserCascadeDelete() {
        User user = userRepository.findById(1L).get();

        userRepository.delete(user);

        Assertions.assertThat(viewedProtectingReportRepository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("보호글 삭제시 최근 본 보호글 삭제 여부 확인")
    void ProtectingReportCascadeDelete() {
        ProtectingReport protectingReport = protectingReportRepository.findById(1L).get();

        protectingReportRepository.delete(protectingReport);

        Assertions.assertThat(viewedProtectingReportRepository.findById(1L)).isEmpty();
    }

    @Test
    void findByUserAndProtectingReport() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("1234567")
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
                .weight(String.valueOf(3.5F))
                .age(String.valueOf((short)2024))
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

        Assertions.assertThat(foundReport.isPresent()).isTrue();
        Assertions.assertThat(viewedProtectingReport.getId()).isEqualTo(foundReport.get().getId());
    }

}