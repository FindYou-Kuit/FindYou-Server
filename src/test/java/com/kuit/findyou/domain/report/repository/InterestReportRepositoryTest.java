package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class InterestReportRepositoryTest {

    @Autowired private InterestReportRepository interestReportRepository;
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

        Image image1 = Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString());
        Image image2 = Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString());
        List<Image> images = new ArrayList<>();
        images.add(image1);
        images.add(image2);


        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);

        InterestReport viewedReport = InterestReport.createInterestReport(user, report);
        interestReportRepository.save(viewedReport);

        User findUser = userRepository.findById(user.getId()).get();
        Report findReport = reportRepository.findById(viewedReport.getId()).get();

        for(InterestReport interestReport : findUser.getInterestReports()) {
            System.out.println(interestReport.getReport().getEventDate());
        }

        for(InterestReport interestReport : findReport.getInterestReports()) {
            System.out.println(interestReport.getReport().getEventDate());
        }
    }

}