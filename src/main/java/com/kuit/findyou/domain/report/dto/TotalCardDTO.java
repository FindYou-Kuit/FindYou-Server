package com.kuit.findyou.domain.report.dto;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Getter
@Builder
public class TotalCardDTO {

    private List<Card> cards;
    private Long lastProtectId;
    private Long lastReportId;
    private Boolean isLast;

    public static TotalCardDTO newInstanceWithShuffle(List <Card> cards, Long newLastProtectId, Long newLastReportId, Boolean isLast){
        Collections.shuffle(cards);

        return TotalCardDTO.builder()
                .cards(cards)
                .lastProtectId(newLastProtectId)
                .lastReportId(newLastReportId)
                .isLast(isLast)
                .build();

    }

    public static TotalCardDTO mergeCards(List<ProtectingReport> protectingReportList, List<Report> reportList, User loginedUser) {
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

    private static Boolean isLast(List<ProtectingReport> protectingReportList, List<Report> reportList) {
        return protectingReportList.size() + reportList.size() <= 20;
    }

}
