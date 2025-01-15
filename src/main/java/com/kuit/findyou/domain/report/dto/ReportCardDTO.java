package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReportCardDTO {

    private List<Card> cards;
    private Long lastReportId;
    private Boolean isLast;
}
