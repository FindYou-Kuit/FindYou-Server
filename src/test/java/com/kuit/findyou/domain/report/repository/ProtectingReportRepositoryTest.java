package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class ProtectingReportRepositoryTest {

    @Autowired ProtectingReportRepository protectingReportRepository;

    @Test
    void save() {
        ProtectingReport report = ProtectingReport.builder()
                .happenDate(LocalDate.now())
                .imageUrl("image.jpg")
                .species("개")
                .noticeNumber("12345123")
                .noticeStartDate(LocalDate.now())
                .noticeEndDate(LocalDate.now())
                .breed("시츄")
                .furColor("갈색")
                .weight(String.valueOf(3.5F))
                .age(String.valueOf((short)2024))
                .sex(Sex.M)
                .neutering(Neutering.N)
                .foundLocation("우리집 앞")
                .significant("눈이 아파보임")
                .careName("행운사")
                .careAddr("용산구")
                .careTel("02-1234-1234")
                .authority("관할서")
                .authorityPhoneNumber("02-111-4312")
                .build();

        protectingReportRepository.save(report);
    }

}