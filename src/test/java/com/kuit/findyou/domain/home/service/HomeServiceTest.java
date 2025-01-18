package com.kuit.findyou.domain.home.service;

import com.kuit.findyou.domain.home.dto.GetHomeDataResponse;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class HomeServiceTest {
    @Autowired
    private HomeService homeService;

    @Autowired
    private ProtectingReportRepository protectingReportRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Test
    void testGetHomeData(){
        // given
        final int PROTECT_NUM = 10;
        ProtectingReport lastSaved = null;
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
                    .weight(50.2F)
                    .age((short) 10)
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
            lastSaved = protectingReportRepository.save(protectingReport);
        }

        // when
        GetHomeDataResponse homeData = homeService.getHomeData();

        // then
        assertThat(homeData.getProtectAnimalCards().get(0).getProtectId()).isEqualTo(lastSaved.getId());
        assertThat(homeData.getTodayRescuedAnimalCount()).isEqualTo(PROTECT_NUM);
    }
}
