package com.kuit.findyou.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserReportCard {
    private Long reportId;
    private String thumbnailImageUrl;
    private String title;
    private String tag;
    private LocalDate date;
    private String location;
}
