package com.kuit.findyou.domain.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class RetrieveViewedAnimalRequestDTO {

    @NotNull
    @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 최근 본 보호글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 최근 본 보호글들을 조회하여 응답합니다.")
    private Long lastViewedProtectId;

    @NotNull
    @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 최근 본 신고글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 최근 본 신고글들을 조회하여 응답합니다.")
    private Long lastViewedReportId;
}
