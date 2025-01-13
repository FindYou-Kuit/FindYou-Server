package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.model.ViewedReport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class ViewedReportRepositoryTest {

    @Autowired private ViewedReportRepository viewedReportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReportRepository reportRepository;
    @Autowired private BreedRepository breedRepository;
    @Autowired private ReportAnimalRepository reportAnimalRepository;

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

        reportAnimalRepository.save(reportAnimal);

        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal);
        reportRepository.save(report);

        ViewedReport viewedReport = ViewedReport.createViewedReport(user, report);
        viewedReportRepository.save(viewedReport);
    }

}