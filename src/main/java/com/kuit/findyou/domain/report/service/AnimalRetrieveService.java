package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.TotalCardDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalRetrieveService {

    private final UserRepository userRepository;
    private final ProtectingReportRepository protectingReportRepository;
    private final ReportRepository reportRepository;

//    public TotalCardDTO retrieveTotalCards(Long userId, Long lastProtectId, Long lastReportId) {
//        User loginedUser = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//
//        Slice<ProtectingReport> protectingReportSlice = protectingReportRepository.findByIdLessThanOrderByIdDesc(lastProtectId, PageRequest.of(0, 20));
//        List<ProtectingReport> protectingReportList = protectingReportSlice.getContent();
//
//        Slice<Report> reportSlice = reportRepository.findByIdLessThanOrderByIdDesc(lastReportId, PageRequest.of(0, 20));
//        List<Report> reportList = reportSlice.getContent();
//
//        return mergeCards(protectingReportList, reportList, loginedUser);
//    }

    public TotalCardDTO retrieveTotalCardsWithFilters(
            Long userId,
            Long lastProtectId,
            Long lastReportId,
            LocalDate startDate,
            LocalDate endDate,
            String species,
            List<String> breeds,
            String location) {

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Slice<ProtectingReport> protectingReportSlice = protectingReportRepository.findProtectingReportWithFilters(lastProtectId,startDate, endDate, species, breeds, location, PageRequest.of(0, 20));
        List<ProtectingReport> protectingReportList = protectingReportSlice.getContent();

        Slice<Report> reportSlice = reportRepository.findReportsWithFilters(lastReportId,startDate, endDate, species, breeds, location, PageRequest.of(0, 20));
        List<Report> reportList = reportSlice.getContent();

        return mergeCards(protectingReportList, reportList, loginedUser);
    }



    private TotalCardDTO mergeCards(List<ProtectingReport> protectingReportList, List<Report> reportList, User loginedUser) {
        List<Card> cards = new ArrayList<>();

        int protectingReportIndex = 0;
        int reportIndex = 0;

        Boolean isLast = isLast(protectingReportList, reportList);

        Long newLastProtectId = -1L;
        Long newLastReportId = -1L;

        while (protectingReportIndex <= protectingReportList.size() - 1 && reportIndex <= reportList.size() - 1) {
            ProtectingReport protectingReport = protectingReportList.get(protectingReportIndex);
            Report report = reportList.get(reportIndex);

            newLastProtectId = protectingReport.getId();
            newLastReportId = report.getId();

            Card protectingCard = Card.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser);

            Card reportCard = Card.newInstanceFromReportWithUser(report, loginedUser);

            cards.add(protectingCard);
            cards.add(reportCard);

            protectingReportIndex++;
            reportIndex++;

            if (cards.size() == 20) {
                return TotalCardDTO.newInstanceWithShuffle(cards, newLastProtectId, newLastReportId, isLast);
            }
        }

        while (protectingReportIndex <= protectingReportList.size() - 1) {
            ProtectingReport protectingReport = protectingReportList.get(protectingReportIndex);

            newLastProtectId = protectingReport.getId();

            Card protectingCard = Card.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser);

            cards.add(protectingCard);

            protectingReportIndex++;

            if (cards.size() == 20) {
                return TotalCardDTO.newInstanceWithShuffle(cards, newLastProtectId, newLastReportId, isLast);
            }
        }

        while (reportIndex <= reportList.size() - 1) {
            Report report = reportList.get(reportIndex);

            newLastReportId = report.getId();

            Card reportCard = Card.newInstanceFromReportWithUser(report, loginedUser);

            cards.add(reportCard);

            reportIndex++;

            if (cards.size() == 20) {
                return TotalCardDTO.newInstanceWithShuffle(cards, newLastProtectId, newLastReportId, isLast);
            }
        }

        return TotalCardDTO.newInstanceWithShuffle(cards, newLastProtectId, newLastReportId, isLast);
    }


    private Boolean isLast(List<ProtectingReport> protectingReportList, List<Report> reportList) {
        if (protectingReportList.size() + reportList.size() <= 20) {
            return true;
        }
        return false;
    }


}
