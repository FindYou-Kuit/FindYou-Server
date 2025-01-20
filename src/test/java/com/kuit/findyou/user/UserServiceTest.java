package com.kuit.findyou.user;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.exception.AlreadySavedInterestException;
import com.kuit.findyou.domain.user.service.UserService;
import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProtectingReportRepository protectingReportRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private ReportAnimalRepository reportAnimalRepository;

    @Autowired
    private AnimalFeatureRepository animalFeatureRepository;

    @Autowired
    private ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;



    @Test
    void saveInterestProtectingAnimalTest(){
        // given
        User user = User.builder()
                .name("홍길동")
                .email("email@email")
                .password("password")
                .profileImageUrl("image.png")
                .build();

        User savedUser = userRepository.save(user);

        ProtectingReport protect = ProtectingReport.builder()
                .happenDate(LocalDate.now())
                .imageUrl("image.png")
                .species("개")
                .noticeNumber("1111")
                .noticeStartDate(LocalDate.now())
                .noticeEndDate(LocalDate.now().plusDays(5))
                .breed("도베르만")
                .furColor("갈색")
                .weight(50.2F)
                .age((short) 10)
                .sex(Sex.M)
                .neutering(Neutering.Y)
                .foundLocation("잠실고 정문 앞")
                .significant("없음")
                .careName("무슨보호소")
                .careAddr("서울시 송파구")
                .careTel("010-1111-1111")
                .authority("송파구청")
                .authorityPhoneNumber("010-1111-1111")
                .build();

        ProtectingReport savedProtect = protectingReportRepository.save(protect);

        Long correctUserId = savedUser.getId();
        Long incorrectUserId = savedUser.getId() + 1L;
        Long existentProtectId = savedProtect.getId();
        Long nonExistentProtectId = savedProtect.getId() + 1L;
        String existentTag = ReportTag.PROTECTING.getValue();
        String nonExistentTag = "보호하라";
        PostInterestAnimalRequest correctRequest = new PostInterestAnimalRequest(existentProtectId, existentTag);
        PostInterestAnimalRequest incorrectIdRequest = new PostInterestAnimalRequest(nonExistentProtectId, existentTag);
        PostInterestAnimalRequest incorrectTagRequest = new PostInterestAnimalRequest(nonExistentProtectId, nonExistentTag);

        userService.saveInterestAnimal(correctUserId, correctRequest);

        // when
        // then
        assertThatThrownBy(() -> userService.saveInterestAnimal(correctUserId, correctRequest)).isInstanceOf(AlreadySavedInterestException.class);
        assertThatThrownBy(() -> userService.saveInterestAnimal(correctUserId, incorrectIdRequest)).isInstanceOf(ReportNotFoundException.class);
        assertThatThrownBy(() -> userService.saveInterestAnimal(correctUserId, incorrectTagRequest)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> userService.saveInterestAnimal(incorrectUserId, correctRequest)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveInterestReportAnimalTest(){
        // given
        User user = User.builder()
                .name("홍길동")
                .email("email@email")
                .password("password")
                .profileImageUrl("image.png")
                .build();

        User savedUser = userRepository.save(user);

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

        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);

        ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
        ReportedAnimalFeature reportedAnimalFeature2 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);

        reportedAnimalFeatureRepository.save(reportedAnimalFeature);
        reportedAnimalFeatureRepository.save(reportedAnimalFeature2);

        // 이미지 객체 생성
        List<Image> images = new ArrayList<>();
        images.add(Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString()));
        images.add(Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString()));

        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, images);
        Report savedReport = reportRepository.save(report);

        Long correctUserId = savedUser.getId();
        Long incorrectUserId = savedUser.getId() + 1L;
        Long existentProtectId = savedReport.getId();
        Long nonExistentProtectId = savedReport.getId() + 1L;
        String existentTag = ReportTag.MISSING.getValue();
        String nonExistentTag = "실종이야";
        PostInterestAnimalRequest correctRequest = new PostInterestAnimalRequest(existentProtectId, existentTag);
        PostInterestAnimalRequest incorrectIdRequest = new PostInterestAnimalRequest(nonExistentProtectId, existentTag);
        PostInterestAnimalRequest incorrectTagRequest = new PostInterestAnimalRequest(nonExistentProtectId, nonExistentTag);

        userService.saveInterestAnimal(correctUserId, correctRequest);

        // when
        // then
        assertThatThrownBy(() -> userService.saveInterestAnimal(correctUserId, correctRequest)).isInstanceOf(AlreadySavedInterestException.class);
        assertThatThrownBy(() -> userService.saveInterestAnimal(correctUserId, incorrectIdRequest)).isInstanceOf(ReportNotFoundException.class);
        assertThatThrownBy(() -> userService.saveInterestAnimal(correctUserId, incorrectTagRequest)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> userService.saveInterestAnimal(incorrectUserId, correctRequest)).isInstanceOf(UserNotFoundException.class);
    }
}
