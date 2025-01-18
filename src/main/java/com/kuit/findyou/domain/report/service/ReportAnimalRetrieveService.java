package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ReportCardDTO;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAnimalRetrieveService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

//    public ReportCardDTO retrieveReportCards(Long userId, Long lastReportId) {
//        User loginedUser = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//
//        Slice<Report> reportSlice = reportRepository.findByIdLessThanOrderByIdDesc(lastReportId, PageRequest.of(0, 20));
//
//        List<Card> cards = reportSlice.map(report ->
//                Card.newInstanceFromReportWithUser(report, loginedUser))
//                .getContent();
//
//        Boolean isLast = reportSlice.isLast();
//
//        Long newLastReportId = -1L;
//        if(!cards.isEmpty()) {
//            newLastReportId = cards.get(cards.size() - 1).getId();
//        }
//
//        return ReportCardDTO.newInstance(cards, newLastReportId, isLast);
//    }

    public ReportCardDTO retrieveReportCardsWithFilters(
            Long userId,
            Long lastReportId,
            LocalDate startDate,
            LocalDate endDate,
            String species,
            List<String> breeds,
            String location) {

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Slice<Report> reportSlice = reportRepository.findReportsWithFilters(lastReportId, startDate, endDate, species, breeds, location, PageRequest.of(0, 20));

        List<Card> cards = reportSlice.map(report ->
                        Card.newInstanceFromReportWithUser(report, loginedUser))
                .getContent();

        Boolean isLast = reportSlice.isLast();

        Long newLastReportId = -1L;
        if(!cards.isEmpty()) {
            newLastReportId = cards.get(cards.size() - 1).getCardId();
        }

        return ReportCardDTO.newInstance(cards, newLastReportId, isLast);
    }
}
