package com.kuit.findyou.domain.user;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.exception.AlreadySavedInterestException;
import com.kuit.findyou.domain.user.service.UserService;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private InterestReportRepository interestReportRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private InterestProtectingReportRepository interestProtectingReportRepository;


    @Test
    void saveInterestProtectingAnimalTest() {
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
        PostInterestAnimalRequest incorrectTagRequest = new PostInterestAnimalRequest(existentProtectId, nonExistentTag);

        userService.saveInterestProtectingAnimal(correctUserId, correctRequest);

        // when
        // then
        assertThatThrownBy(() -> userService.saveInterestProtectingAnimal(correctUserId, correctRequest)).isInstanceOf(AlreadySavedInterestException.class);
        assertThatThrownBy(() -> userService.saveInterestProtectingAnimal(correctUserId, incorrectIdRequest)).isInstanceOf(ReportNotFoundException.class);
//        assertThatThrownBy(() -> userService.saveInterestProtectingAnimal(correctUserId, incorrectTagRequest)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> userService.saveInterestProtectingAnimal(incorrectUserId, correctRequest)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void saveInterestReportAnimalTest() {
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
//        PostInterestAnimalRequest incorrectTagRequest = new PostInterestAnimalRequest(nonExistentProtectId, nonExistentTag);

        userService.saveInterestReportAnimal(correctUserId, correctRequest);

        // when
        // then
        assertThatThrownBy(() -> userService.saveInterestReportAnimal(correctUserId, correctRequest)).isInstanceOf(AlreadySavedInterestException.class);
        assertThatThrownBy(() -> userService.saveInterestReportAnimal(correctUserId, incorrectIdRequest)).isInstanceOf(ReportNotFoundException.class);
//        assertThatThrownBy(() -> userService.saveInterestReportAnimal(correctUserId, incorrectTagRequest)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> userService.saveInterestReportAnimal(incorrectUserId, correctRequest)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("관심 신고동물 삭제 테스트")
    void removeInterestReportAnimalTest() {
        //given
        User user = User.builder()
                .name("홍길동")
                .email("email@email")
                .password("password")
                .profileImageUrl("image.png")
                .build();
        Long savedUserId = userRepository.save(user).getId();

        Breed breed = Breed.builder()
                .name("치와와")
                .species("개")
                .build();
        breedRepository.save(breed);

        // 신고동물 생성
        ReportAnimal reportAnimal = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();
        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal, null);
        Report savedReport = reportRepository.save(report);
        PostInterestAnimalRequest request = PostInterestAnimalRequest.builder()
                .id(savedReport.getId())
                .tag(ReportTag.WITNESSED.getValue())
                .build();
        Long savedInterestId = userService.saveInterestReportAnimal(savedUserId, request);

        ReportAnimal reportAnimal2 = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();
        Report report2 = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal2, null);
        Report savedReport2 = reportRepository.save(report2);
        PostInterestAnimalRequest request2 = PostInterestAnimalRequest.builder()
                .id(savedReport2.getId())
                .tag(ReportTag.WITNESSED.getValue())
                .build();
        Long savedInterestId2 = userService.saveInterestReportAnimal(savedUserId, request2);

        ReportAnimal reportAnimal3 = ReportAnimal.builder()
                .furColor("흰색, 검은색")
                .breed(breed)
                .build();
        Report report3 = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal3, null);
        Report savedReport3 = reportRepository.save(report3);
        PostInterestAnimalRequest request3 = PostInterestAnimalRequest.builder()
                .id(savedReport3.getId())
                .tag(ReportTag.WITNESSED.getValue())
                .build();
        Long savedInterestId3 = userService.saveInterestReportAnimal(savedUserId, request3);

        // when
        userService.removeInterestReportAnimal(savedUserId, savedInterestId2);

        em.flush();
        em.clear();


        Optional<InterestReport> interestReportById = interestReportRepository.findById(savedInterestId2);
        Optional<User> userById = userRepository.findById(savedUserId);

        // then
        Assertions.assertThat(interestReportById.isPresent()).isEqualTo(false);
        Assertions.assertThat(userById.get().getInterestReports()).hasSize(2);
    }

    @Test
    @DisplayName("관심 보호중 동물 삭제")
    void removeInterestProtectingReport() {
        // given
        User user = User.builder()
                .name("홍길동")
                .email("email@email")
                .password("password")
                .profileImageUrl("image.png")
                .build();

        Long userId = userRepository.save(user).getId();

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
        PostInterestAnimalRequest request = new PostInterestAnimalRequest(savedProtect.getId(), ReportTag.PROTECTING.getValue());
        userService.saveInterestProtectingAnimal(userId, request);

        ProtectingReport protect2 = ProtectingReport.builder()
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

        savedProtect = protectingReportRepository.save(protect2);
        request = new PostInterestAnimalRequest(savedProtect.getId(), ReportTag.PROTECTING.getValue());
        userService.saveInterestProtectingAnimal(userId, request);

        ProtectingReport protect3 = ProtectingReport.builder()
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

        savedProtect = protectingReportRepository.save(protect3);
        request = new PostInterestAnimalRequest(savedProtect.getId(), ReportTag.PROTECTING.getValue());
        Long interestProtectingId = userService.saveInterestProtectingAnimal(userId, request);

        Assertions.assertThat(interestProtectingReportRepository.findAll()).hasSize(3);

        // when
        userService.removeInterestProtectingAnimal(userId, interestProtectingId);

        em.flush();
        em.clear();

        User foundUser = userRepository.findById(userId).get();
        boolean exists = interestProtectingReportRepository.existsById(interestProtectingId);

        // then
        Assertions.assertThat(foundUser.getInterestProtectingReports()).hasSize(2);
        Assertions.assertThat(exists).isEqualTo(false);
    }

    @Test
    @DisplayName("닉네임 변경 테스트")
    void updateNickname() {
        User user = User.builder()
                .name("홍길동")
                .email("email@email")
                .password("password")
                .profileImageUrl("image.png")
                .build();

        User savedUser = userRepository.save(user);

        // when
        userService.updateNickname(savedUser.getId(), "아무개");

        em.flush();
        em.clear();

        // then
        User findUser = userRepository.findById(savedUser.getId()).get();
        assertThat(findUser.getName()).isEqualTo("아무개");
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void deleteUser() {
        User user = User.builder()
                .name("홍길동")
                .email("email@email")
                .password("password")
                .profileImageUrl("image.png")
                .build();

        User savedUser = userRepository.save(user);

        userService.deleteUser(savedUser.getId());
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }
}
