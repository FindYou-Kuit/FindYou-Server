package com.kuit.findyou.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Schema(description = "조회 결과를 담은 DTO 객체")
@Getter
@Builder
public class GetInterestAnimalCursorPageDto {
    @Schema(description = "관심 동물 리스트")
    private List<InterestAnimalCard> interestAnimals;
    @Schema(description = "다음 조회 시에 사용할 id값")
    private Long lastInterestProtectId;
    @Schema(description = "다음 조회 시에 사용할 id값")
    private Long lastInterestReportId;
    @Schema(description = "마지막 페이지인지 여부")
    private Boolean isLast;
}
