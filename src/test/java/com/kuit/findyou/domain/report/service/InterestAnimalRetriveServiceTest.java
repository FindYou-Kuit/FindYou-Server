package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.user.dto.GetInterestAnimalCursorPageDto;
import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.service.InterestAnimalRetrieveService;
import com.kuit.findyou.domain.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class InterestAnimalRetriveServiceTest {
    @Autowired
    private InterestAnimalRetrieveService interestAnimalRetrieveService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProtectingReportRepository protectingReportRepository;
    @Autowired
    private InterestProtectingReportRepository interestProtectingReportRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager em;
    @Test
    @DisplayName("관심동물 조회 테스트")
    void getInterestAnimalCursorPageTest(){
        // given
        final int REPORT_NUM = 10;
        User user = buildUser();
        User savedUser = userRepository.save(user);
        ProtectingReport savedReport = null;
        for (int i = 1; i <= REPORT_NUM; i++) {
            ProtectingReport report = buildProtectingReport(i);
            savedReport = protectingReportRepository.save(report);
            PostInterestAnimalRequest request = PostInterestAnimalRequest.builder().id(savedReport.getId()).build();
            userService.saveInterestProtectingAnimal(savedUser.getId(), request);
        }
        Assertions.assertThat(interestProtectingReportRepository.findAllByUserId(savedUser.getId())).hasSize(REPORT_NUM);
        // when
        GetInterestAnimalCursorPageDto cursorPage = interestAnimalRetrieveService.getInterestAnimalCursorPage(savedUser.getId(), Long.MAX_VALUE, Long.MAX_VALUE, 20);

        Assertions.assertThat(cursorPage.getIsLast()).isTrue();
        Assertions.assertThat(cursorPage.getLastInterestProtectId()).isNotEqualTo(-1);
        Assertions.assertThat(cursorPage.getLastInterestReportId()).isEqualTo(Long.MAX_VALUE);
        Assertions.assertThat(cursorPage.getInterestAnimals()).hasSize(REPORT_NUM);
    }

    private ProtectingReport buildProtectingReport(int i) {
        return ProtectingReport.builder()
                .happenDate(LocalDate.now())
                .imageUrl(String.valueOf(i))
                .species(String.valueOf(i))
                .noticeNumber(String.valueOf(i))
                .noticeStartDate(LocalDate.now())
                .noticeEndDate(LocalDate.now())
                .breed(String.valueOf(i))
                .furColor(String.valueOf(i))
                .weight(String.valueOf(3.5F))
                .age(String.valueOf((short) i))
                .sex(Sex.M)
                .neutering(Neutering.N)
                .foundLocation(String.valueOf(i))
                .significant(String.valueOf(i))
                .careName(String.valueOf(i))
                .careAddr(String.valueOf(i))
                .careTel(String.valueOf(i))
                .authority(String.valueOf(i))
                .authorityPhoneNumber(String.valueOf(i))
                .build();

    }

    private User buildUser() {
       return User.builder()
                .name("me")
                .password("1234")
                .profileImageUrl("image.png")
                .email("me@me")
                .build();
    }
}
