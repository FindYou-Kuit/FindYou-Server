package com.kuit.findyou.domain.home.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.GetHomeDataResponse;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class HomeServiceTest {
    @Autowired
    private HomeService homeService;

    @Autowired
    private ProtectingReportRepository protectingReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private AnimalFeatureRepository animalFeatureRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void testGetHomeData(){
        // given

        // 보호중 글 생성
        final int PROTECT_NUM = 10;
        ProtectingReport lastSavedProtect = null;
        for(int i = 1; i <= PROTECT_NUM; i++){
            ProtectingReport protectingReport = ProtectingReport.builder()
                    .happenDate(LocalDate.now())
                    .imageUrl("image.png")
                    .species("개" + i)
                    .noticeNumber(Integer.toString(i))
                    .noticeStartDate(LocalDate.now())
                    .noticeEndDate(LocalDate.now().plusDays(5))
                    .breed("도베르만")
                    .furColor("갈색")
                    .weight(String.valueOf(50.2F))
                    .age(String.valueOf(10))
                    .sex(Sex.M)
                    .neutering(Neutering.Y)
                    .foundLocation("잠실고 정문 앞")
                    .significant("없음")
                    .careName("무슨보호소" + i)
                    .careAddr("서울시 송파구")
                    .careTel("010-1111-1111")
                    .authority("송파구청")
                    .authorityPhoneNumber("010-1111-1111")
                    .build();
            lastSavedProtect = protectingReportRepository.save(protectingReport);
        }

        // 신고글 생성
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("1234567")
                .build();
        userRepository.save(user);

        Breed breed = Breed.builder()
                .name("치와와")
                .species("개")
                .build();
        breedRepository.save(breed);

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        animalFeatureRepository.save(animalFeature);

        final int REPORT_NUM = 20;
        Report lastSavedReport = null;
        for(int i = 1; i<= REPORT_NUM; i++){
            ReportAnimal reportAnimal = ReportAnimal.builder()
                    .furColor("흰색, 검은색" + i)
                    .breed(breed)
                    .build();

            ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);

            List<Image> images = new ArrayList<>();
            images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
            images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

            images.forEach(imageRepository::save);

            Report report = Report.createReport(ReportTag.WITNESSED, "내집앞" + i, LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
            lastSavedReport = reportRepository.save(report);
        }

        // when
        GetHomeDataResponse homeData = homeService.getHomeData();

        // then
        assertThat(homeData.getProtectAnimalCards().get(0).getProtectId()).isEqualTo(lastSavedProtect.getId());
        assertThat(homeData.getTodayRescuedAnimalCount()).isEqualTo(PROTECT_NUM);

        assertThat(homeData.getReportAnimalCards().get(0).getReportId()).isEqualTo(lastSavedReport.getId());
        assertThat(homeData.getTodayReportAnimalCount()).isEqualTo(REPORT_NUM);
    }
}
