package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired private BreedRepository breedRepository;
    @Autowired private ReportAnimalRepository reportAnimalRepository;
    @Autowired private AnimalFeatureRepository animalFeatureRepository;
    @Autowired private ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;


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

        ReportAnimal reportAnimal2 = ReportAnimal.builder()
                .furColor("갈색")
                .breed(breed)
                .build();
        reportAnimalRepository.save(reportAnimal2);

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature reportedAnimalFeature2 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);
        ReportedAnimalFeature reportedAnimalFeature3 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal2, animalFeature);
        ReportedAnimalFeature reportedAnimalFeature4 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal2, animalFeature2);

        reportedAnimalFeatureRepository.save(reportedAnimalFeature);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature2);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature3);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature4);


        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal);
        Report report2 = Report.createReport("실종 신고", "여자친구 집 앞", LocalDate.now(), "못생김", user, reportAnimal2);
        reportRepository.save(report);
        reportRepository.save(report2);


        Report findReport = reportRepository.findById(report.getId()).get();
        ReportAnimal findAnimal = findReport.getReportAnimal();
        for(ReportedAnimalFeature reportedAnimalFeature1 : findAnimal.getReportedAnimalFeatures()) {
            System.out.println(reportedAnimalFeature1.getFeature().getFeatureValue());
        }

        User findUser = userRepository.findById(user.getId()).get();
        for(Report report1 : findUser.getReports()) {
            System.out.println(report1.getEventLocation());
        }

    }


}