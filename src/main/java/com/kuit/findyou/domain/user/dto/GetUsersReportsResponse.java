package com.kuit.findyou.domain.user.dto;

import com.kuit.findyou.domain.report.dto.Card;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetUsersReportsResponse {
    private List<UserReportCard> reports;
    private Long lastReportId;
    private Boolean isLast;
}
