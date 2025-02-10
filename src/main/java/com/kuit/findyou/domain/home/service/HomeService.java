package com.kuit.findyou.domain.home.service;

import com.kuit.findyou.domain.home.dto.GetHomeDataResponse;
import com.kuit.findyou.domain.home.dto.HomeProtectAnimalCard;
import com.kuit.findyou.domain.home.dto.HomeReportAnimalCard;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final ProtectingReportRepository protectingReportRepository;
    private final ReportRepository reportRepository;
    public GetHomeDataResponse getHomeData() {
        Long todayProtectCount = protectingReportRepository.countByHappenDateEquals(LocalDate.now().minusDays(1)); // 오늘 추가된 보호글 개수 계산
        Long todayReportCount = reportRepository.countByCreatedAtBetween(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX));  // 오늘 추가된 신고글 개수 계산
        List<ProtectingReport> recent10Protects = protectingReportRepository.findTop10ByOrderByCreatedAtDesc();
        List<Report> recent10Reports = reportRepository.findTop10ByOrderByCreatedAtDesc();

        // dto 반환
        List<HomeProtectAnimalCard> protectAnimalCards = recent10Protects.stream().map(HomeProtectAnimalCard::entityToDto).collect(toList());
        List<HomeReportAnimalCard> reportAnimalCards = recent10Reports.stream().map(HomeReportAnimalCard::entityToDto).collect(toList());
        return GetHomeDataResponse.builder()
                .todayRescuedAnimalCount(todayProtectCount)
                .todayReportAnimalCount(todayReportCount)
                .protectAnimalCards(protectAnimalCards)
                .reportAnimalCards(reportAnimalCards)
                .build();
    }
}
