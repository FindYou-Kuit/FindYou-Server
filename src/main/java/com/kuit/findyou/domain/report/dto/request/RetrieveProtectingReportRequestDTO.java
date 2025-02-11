package com.kuit.findyou.domain.report.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ParameterObject
public class RetrieveProtectingReportRequestDTO {

    @NotNull
    @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 보호글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 보호글들을 조회하여 응답합니다.")
    private Long lastProtectId;

    @Parameter(description = "시작일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Parameter(description = "종료일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Parameter(description = "축종입니다. 개, 고양이, 기타 동물 중 하나여야합니다.")
    private String species;

    @Parameter(description = "품종입니다. 쉼표로 구분된 문자열로 입력해주세요.", example = "치와와,말티즈")
    private String breeds;

    @Parameter(description = "장소입니다. 관할구역에 해당됩니다.")
    private String location;
}
