package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Card {

    private Long id;
    private String thumbnailImageUrl;
    private String title;
    private String tag;
    private String date;
    private String location;
    private Boolean interest;

}
