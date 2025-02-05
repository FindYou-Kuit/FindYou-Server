package com.kuit.findyou.domain.report.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ParameterObject
public class RetrieveAllRequest {

    @NotNull
    @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 보호글의 ID입니다.")
    private Long lastProtectId;

    @NotNull
    @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 신고글의 ID입니다.")
    private Long lastReportId;

    @Parameter(description = "시작일입니다. yyyy-MM-dd 형식으로 입력해야 합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Parameter(description = "종료일입니다. yyyy-MM-dd 형식으로 입력해야 합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Parameter(description = "축종입니다. 개, 고양이, 기타 동물 중 하나여야 합니다.")
    private String species;

    @Parameter(description = "품종입니다. 쉼표로 구분된 문자열로 입력해주세요. 예: 치와와,말티즈")
    private String breeds;

    @Parameter(description = "장소입니다. 태그에 따라 각각 관할구역, 목격장소, 실종장소에 해당됩니다.")
    private String location;
}


