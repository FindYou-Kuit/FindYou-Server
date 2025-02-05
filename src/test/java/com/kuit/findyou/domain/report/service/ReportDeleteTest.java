package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ReportAnimal;
import com.kuit.findyou.domain.report.repository.BreedRepository;
import com.kuit.findyou.domain.report.repository.ReportAnimalRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReportDeleteTest {

    @Autowired private ReportDeleteService reportDeleteService;
    @Autowired private ReportRepository reportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BreedRepository breedRepository;
    @Autowired private ReportAnimalRepository animalRepository;

    private Report report;
    private User user;
    private Breed breed;
    private ReportAnimal reportAnimal;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("정주연")
                .email("jjuyaa@naver.com")
                .password("123123")
                .build();
        userRepository.save(user);
        ReportTag tag = ReportTag.MISSING;
        LocalDate eventDate = LocalDate.now();

        breed = Breed.builder()
                .name("말티즈")
                .species("개")
                .build();
        breedRepository.save(breed);

        reportAnimal = ReportAnimal.builder()
                .furColor("흰색")
                .sex("M")
                .breed(breed)
                .build();

        report = Report.builder()
                .tag(tag)
                .eventLocation("서울")
                .eventDate(eventDate)
                .user(user)
                .reportAnimal(reportAnimal)
                .build();

        report = reportRepository.save(report);
    }

    @Test
    void deleteReport_Success() {
        reportDeleteService.deleteReport(report.getId());

        // 삭제 후 데이터베이스에서 해당 객체가 없는지 확인
        assertThat(reportRepository.findById(report.getId())).isEmpty();

    }

}
