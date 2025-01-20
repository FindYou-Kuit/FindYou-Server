package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.*;
import org.assertj.core.api.Assertions;
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
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class ViewedReportRepositoryTest {

    @Autowired private ViewedReportRepository viewedReportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReportRepository reportRepository;
    @Autowired private BreedRepository breedRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
                .build();

        userRepository.save(user);

        Breed breed = Breed.builder()
                .name("치와와")
                .species("개")
                .build();

        breedRepository.save(breed);


        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();

        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);

        ViewedReport viewedReport = ViewedReport.createViewedReport(user, report);
        viewedReportRepository.save(viewedReport);
    }


    @Test
    void save() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
                .build();

        userRepository.save(user);

        Breed breed = Breed.builder()
                .name("치와와")
                .species("개")
                .build();

        breedRepository.save(breed);


        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();

        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);

        ViewedReport viewedReport = ViewedReport.createViewedReport(user, report);
        viewedReportRepository.save(viewedReport);
    }

    @Test
    void delete() {
        ViewedReport viewedReport = viewedReportRepository.findById(1L).get();
        viewedReportRepository.delete(viewedReport);
    }

    @Test
    @DisplayName("유저 삭제시 최근 본 신고글 삭제 여부 확인")
    void UserCascadeDelete() {
        User user = userRepository.findById(1L).get();

        userRepository.delete(user);

        Assertions.assertThat(viewedReportRepository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("신고글 삭제시 최근 본 신고글 삭제 여부 확인")
    void ProtectingReportCascadeDelete() {
        Report report = reportRepository.findById(1L).get();

        reportRepository.delete(report);

        Assertions.assertThat(viewedReportRepository.findById(1L)).isEmpty();
    }

    @Test
    void findByUserAndReport() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
                .build();

        userRepository.save(user);

        Breed breed = Breed.builder()
                .name("치와와")
                .species("개")
                .build();

        breedRepository.save(breed);


        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();

        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));


        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);

        ViewedReport viewedReport = ViewedReport.createViewedReport(user, report);
        viewedReportRepository.save(viewedReport);

        Optional<ViewedReport> foundReport = viewedReportRepository.findByUserAndReport(user, report);

        Assertions.assertThat(foundReport.isPresent()).isTrue();
        Assertions.assertThat(viewedReport.getId()).isEqualTo(foundReport.get().getId());


    }

}