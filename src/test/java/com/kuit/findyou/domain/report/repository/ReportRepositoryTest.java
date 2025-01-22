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
import java.util.UUID;

@SpringBootTest
@Transactional
class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired private BreedRepository breedRepository;
    @Autowired private AnimalFeatureRepository animalFeatureRepository;

    @Autowired private ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;
    @Autowired private ReportAnimalRepository reportAnimalRepository;
    @Autowired private ImageRepository imageRepository;

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

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();

        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

        images.forEach(imageRepository::save);
        
        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);
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

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();

        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));


        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        reportRepository.save(report);

        // 신고 동물, 신고 동물 특징, 신고글 이미지 정보를 명시적으로 save 해주지 않아도 연관 관계를 적절히 맺어주고 Report 만 save 하면 자동으로 DB에 insert 되는 것을 확인할 수 있음

//        images.forEach(imageRepository::save);

//        Report findReport = reportRepository.findById(report.getId()).get();
//        ReportAnimal findAnimal = findReport.getReportAnimal();
//        for(ReportedAnimalFeature reportedAnimalFeature1 : findAnimal.getReportedAnimalFeatures()) {
//            System.out.println(reportedAnimalFeature1.getFeature().getFeatureValue());
//        }
//
//        User findUser = userRepository.findById(user.getId()).get();
//        for(Report report1 : findUser.getReports()) {
//            System.out.println(report1.getFoundLocation());
//        }

    }

    @Test
    @DisplayName("신고글 삭제시 신고 동물, 신고 동물 특징, 신고글 이미지 삭제 여부 확인")
    void reportCascadeDelete() {
        Report report = reportRepository.findById(1L).get();
        reportRepository.delete(report);

        Assertions.assertThat(reportAnimalRepository.findById(1L)).isEmpty();
        Assertions.assertThat(reportedAnimalFeatureRepository.findAll()).isEmpty();
        Assertions.assertThat(imageRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("유저 삭제시 신고글까지도 삭제되는지 확인")
    void UserCascadeDelete() {
        User user = userRepository.findById(1L).get();

        userRepository.delete(user);

        Assertions.assertThat(reportRepository.findById(1L)).isEmpty();
        Assertions.assertThat(reportAnimalRepository.findById(1L)).isEmpty();
        Assertions.assertThat(reportedAnimalFeatureRepository.findAll()).isEmpty();
        Assertions.assertThat(imageRepository.findAll()).isEmpty();
    }


}