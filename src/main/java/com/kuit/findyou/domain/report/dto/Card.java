package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Card {

    private Long id;
    private String thumbnailImageUrl;
    private String title;
    private String tag;
    private String date;
    private String location;
    private Boolean interest;

}
