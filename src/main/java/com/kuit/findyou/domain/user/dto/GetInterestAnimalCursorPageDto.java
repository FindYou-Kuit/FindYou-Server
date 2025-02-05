package com.kuit.findyou.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Builder
public class GetInterestAnimalCursorPageDto {
    private List<InterestAnimalCard> interestAnimals;
    private Long lastInterestProtectId;
    private Long lastInterestReportId;
    private Boolean isLast;
}
