package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class ViewedCardDTO {

    @Schema(description = "최근 본 동물들에 대한 카드 리스트")
    private List<Card> viewedAnimals;

    @Schema(description = "페이징을 통해 반환된 최근 본 동물들 중 마지막 최근 본 보호 동물 ID. 다음 요청에 이 값을 전달함으로써 무한스크롤을 구현하면 됩니다.")
    private Long lastViewedProtectId;

    @Schema(description = "페이징을 통해 반환된 최근 본 동물들 중 마지막 최근 본 신고 동물 ID. 다음 요청에 이 값을 전달함으로써 무한스크롤을 구현하면 됩니다.")
    private Long lastViewedReportId;
    private Boolean isLast;

    public static ViewedCardDTO newInstanceWithShuffle(List <Card> cards, Long newLastViewedProtectId, Long newLastViewedReportId, Boolean isLast){
        Collections.shuffle(cards);

        return ViewedCardDTO.builder()
                .viewedAnimals(cards)
                .lastViewedProtectId(newLastViewedProtectId)
                .lastViewedReportId(newLastViewedReportId)
                .isLast(isLast)
                .build();

    }

    public static ViewedCardDTO mergeCards(List<ViewedProtectingReport> viewedProtectingReportList, List<ViewedReport> viewedReportList, User loginedUser) {
        List<Card> viewedAnimals = new ArrayList<>();

        int viewedProtectingReportIndex = 0;
        int viewedReportIndex = 0;

        Boolean isLast = isLast(viewedProtectingReportList, viewedReportList);

        Long newLastViewedProtectId = -1L;
        Long newLastViewedReportId = -1L;

        while (viewedProtectingReportIndex <= viewedProtectingReportList.size() - 1 && viewedReportIndex <= viewedReportList.size() - 1) {
            ViewedProtectingReport viewedProtectingReport = viewedProtectingReportList.get(viewedProtectingReportIndex);
            ViewedReport viewedReport = viewedReportList.get(viewedReportIndex);

            ProtectingReport protectingReport = viewedProtectingReport.getProtectingReport();
            Report report = viewedReport.getReport();

            newLastViewedProtectId = viewedProtectingReport.getId();
            newLastViewedReportId = viewedReport.getId();

            Card protectingCard = Card.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser);

            Card reportCard = Card.newInstanceFromReportWithUser(report, loginedUser);

            viewedAnimals.add(protectingCard);
            viewedAnimals.add(reportCard);

            viewedProtectingReportIndex++;
            viewedReportIndex++;

            if (viewedAnimals.size() == 20) {
                return newInstanceWithShuffle(viewedAnimals, newLastViewedProtectId, newLastViewedReportId, isLast);
            }
        }

        while (viewedProtectingReportIndex <= viewedProtectingReportList.size() - 1) {
            ViewedProtectingReport viewedProtectingReport = viewedProtectingReportList.get(viewedProtectingReportIndex);
            ProtectingReport protectingReport = viewedProtectingReport.getProtectingReport();

            newLastViewedProtectId = viewedProtectingReport.getId();

            Card protectingCard = Card.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser);

            viewedAnimals.add(protectingCard);

            viewedProtectingReportIndex++;

            if (viewedAnimals.size() == 20) {
                return newInstanceWithShuffle(viewedAnimals, newLastViewedProtectId, newLastViewedReportId, isLast);
            }
        }

        while (viewedReportIndex <= viewedReportList.size() - 1) {
            ViewedReport viewedReport = viewedReportList.get(viewedReportIndex);
            Report report = viewedReport.getReport();

            newLastViewedReportId = viewedReport.getId();

            Card reportCard = Card.newInstanceFromReportWithUser(report, loginedUser);

            viewedAnimals.add(reportCard);

            viewedReportIndex++;

            if (viewedAnimals.size() == 20) {
                return newInstanceWithShuffle(viewedAnimals, newLastViewedProtectId, newLastViewedReportId, isLast);
            }
        }

        return newInstanceWithShuffle(viewedAnimals, newLastViewedProtectId, newLastViewedReportId, isLast);
    }

    private static Boolean isLast(List<ViewedProtectingReport> viewedProtectingReportList, List<ViewedReport> viewedReportList) {
        return viewedProtectingReportList.size() + viewedReportList.size() <= 20;
    }
}
