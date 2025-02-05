package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.report.dto.*;
import com.kuit.findyou.domain.report.service.*;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportAnimalInfoService reportAnimalInfoService;
    private final ProtectingAnimalInfoService protectingAnimalInfoService;
    private final ProtectingAnimalRetrieveService protectingAnimalRetrieveService;
    private final ReportAnimalRetrieveService reportAnimalRetrieveService;
    private final AnimalRetrieveService animalRetrieveService;
    private final MissingReportPostService missingReportPostService;
    private final WitnessReportPostService witnessReportPostService;
    private final ReportDeleteService reportDeleteService;
    private final GetAllBreedsService getAllBreedsService;
    private final BreedValidateService breedValidateService;



    @Operation(summary = "신고 동물 정보 상세 조회", description = "특정 신고 동물의 정보를 상세 조회합니다.")
    @GetMapping("/report-animals/{report_id}")
    public BaseResponse<ReportInfoDTO> reportInfo(
            @Parameter(required = true, description = "상세 조회하고자 하는 신고글의 ID(식별자)")
            @PathVariable("report_id") Long reportId) {
        ReportInfoDTO findReportInfo = reportAnimalInfoService.findReportInfoById(reportId, 1L);

        return new BaseResponse<>(findReportInfo);
    }

    @Operation(summary = "구조 동물 정보 상세 조회", description = "특정 구조 동물의 정보를 상세 조회합니다.")
    @GetMapping("/protecting-animals/{protecting_report_id}")
    public BaseResponse<ProtectingReportInfoDTO> protectingReportInfo(
            @Parameter(required = true, description = "상세 조회하고자 하는 보호글의 ID(식별자)")
            @PathVariable("protecting_report_id") Long protectingReportId) {
        ProtectingReportInfoDTO findProtectingInfo = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, 1L);

        return new BaseResponse<>(findProtectingInfo);
    }

    @Operation(summary = "구조 동물 조회", description = "구조 동물들의 정보를 조회합니다.")
    @GetMapping("/protecting-animals")
    public BaseResponse<ProtectingReportCardDTO> retrieveProtectingReports(
            @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 보호글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 보호글들을 조회하여 응답합니다.")
            @RequestParam("lastProtectId") Long lastProtectId,
            @Parameter(description = "시작일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "축종입니다. 개, 고양이, 기타 동물 중 하나여야합니다.")
            @RequestParam(value = "species", required = false) String species,
            @Parameter(description = "품종입니다. 쉼표로 구분된 문자열로 입력해주세요.", example = "치와와,말티즈")
            @RequestParam(value = "breeds", required = false) String breeds,
            @Parameter(description = "장소입니다. 관할구역에 해당됩니다.")
            @RequestParam(value = "location", required = false) String location) {

        List<String> breedList = parseBreeds(breeds);

        ProtectingReportCardDTO protectingReportCardDTO =
                protectingAnimalRetrieveService.retrieveProtectingReportCardsWithFilters(1L, lastProtectId, startDate, endDate, species, breedList, location);

        return new BaseResponse<>(protectingReportCardDTO);
    }

    @Operation(summary = "신고 동물 조회", description = "신고 동물들의 정보를 조회합니다.")
    @GetMapping("/report-animals")
    public BaseResponse<ReportCardDTO> retrieveReports(
            @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 신고글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 신고글들을 조회하여 응답합니다.")
            @RequestParam("lastReportId") Long lastReportId,
            @Parameter(description = "시작일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "축종입니다. 개, 고양이, 기타 동물 중 하나여야합니다.")
            @RequestParam(value = "species", required = false) String species,
            @Parameter(description = "품종입니다. 쉼표로 구분된 문자열로 입력해주세요.", example = "치와와,말티즈")
            @RequestParam(value = "breeds", required = false) String breeds,
            @Parameter(description = "장소입니다. 태그에 따라 각각 목격장소, 실종장소에 해당됩니다.")
            @RequestParam(value = "location", required = false) String location) {

        List<String> breedList = parseBreeds(breeds);

        ReportCardDTO reportCardDTO = reportAnimalRetrieveService.retrieveReportCardsWithFilters(1L, lastReportId, startDate, endDate, species, breedList, location);

        return new BaseResponse<>(reportCardDTO);
    }

    @Operation(summary = "전체 조회", description = "모든 동물들의 정보를 조회합니다.")
    @GetMapping
    public BaseResponse<TotalCardDTO> retrieveAll(
            @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 보호글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 보호글들을 조회하여 응답합니다.")
            @RequestParam("lastProtectId") Long lastProtectId,
            @Parameter(description = "이전 요청을 통해 받아온 데이터들 중 마지막 신고글의 ID 입니다. 이 값을 다음 요청에 포함시키면 그 다음 신고글들을 조회하여 응답합니다.")
            @RequestParam("lastReportId") Long lastReportId,
            @Parameter(description = "시작일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일입니다. yyyy-mm-dd 형식으로 요청을 받아야합니다.")
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "축종입니다. 개, 고양이, 기타 동물 중 하나여야합니다.")
            @RequestParam(value = "species", required = false) String species,
            @Parameter(description = "품종입니다. 쉼표로 구분된 문자열로 입력해주세요.", example = "치와와,말티즈")
            @RequestParam(value = "breeds", required = false) String breeds,
            @Parameter(description = "장소입니다. 태그에 따라 각각 관할구역, 목격장소, 실종장소에 해당됩니다.")
            @RequestParam(value = "location", required = false) String location) {

        List<String> breedList = parseBreeds(breeds);

        TotalCardDTO totalCardDTO = animalRetrieveService.retrieveTotalCardsWithFilters(1L, lastProtectId, lastReportId, startDate, endDate, species, breedList, location);

        return new BaseResponse<>(totalCardDTO);
    }

    @PostMapping("/new-missing-reports")
    public BaseResponse<Void> postMissingReport(@RequestBody MissingReportDTO requestDTO) {
        missingReportPostService.createReport(requestDTO);
        return new BaseResponse<>(null);
    }
    @PostMapping("/new-witness-reports")
    public BaseResponse<Void> postWitnessReport(@RequestBody WitnessReportDTO requestDTO) {
        witnessReportPostService.createReport(requestDTO);
        return new BaseResponse<>(null);
    }

    @DeleteMapping("/api/v1/reports/{report_id}")
    public BaseResponse<Void> deleteReport(@PathVariable("report_id") Long reportId) {
        reportDeleteService.deleteReport(reportId);
        return new BaseResponse<>(null);
    }



    @Operation(summary = "품종 전체 반환", description = "모든 품종 정보를 반환합니다.")
    @GetMapping("/breeds")
    public BaseResponse<List<BreedResponseDTO>> getAllBreeds() {
        return new BaseResponse<>(getAllBreedsService.getAllBreeds());
    }

    @Operation(summary = "품종 검증", description = "입력으로 전달된 품종이 DB에 존재하는지 검증합니다.")
    @GetMapping("/breeds/validation")
    public BaseResponse<BreedValidateResponseDTO> validateBreed(
            @Parameter(required = true, description = "DB에 존재하는 지 검증할 품종")
            @RequestParam String breedName) {
        return new BaseResponse<>(breedValidateService.validateBreed(breedName));
    }


    // 문자열을 쉼표로 분할하고 각 요소의 앞뒤 공백을 제거하는 메서드
    private List<String> parseBreeds(String breeds) {
        if (breeds == null || breeds.isBlank()) {
            return null;
        }
        return Arrays.stream(breeds.split(","))
                .map(String::trim)   // 각 요소의 앞뒤 공백 제거
                .filter(s -> !s.isEmpty())  // 공백만 있는 요소 제거
                .toList();  // 스트림을 리스트로 변환 (Java 16+)
    }
}
