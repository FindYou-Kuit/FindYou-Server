package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.report.dto.*;
import com.kuit.findyou.domain.report.service.*;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
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
    public BaseResponse<ProtectingReportCardDTO> retrieveProtectingReports(@Validated @ModelAttribute RetrieveProtectingReportRequest request) {

        List<String> breedList = parseBreeds(request.getBreeds());

        ProtectingReportCardDTO protectingReportCardDTO =
                protectingAnimalRetrieveService.retrieveProtectingReportCardsWithFilters(
                        1L,
                        request.getLastProtectId(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getSpecies(),
                        breedList,
                        request.getLocation());

        return new BaseResponse<>(protectingReportCardDTO);
    }

    @Operation(summary = "신고 동물 조회", description = "신고 동물들의 정보를 조회합니다.")
    @GetMapping("/report-animals")
    public BaseResponse<ReportCardDTO> retrieveReports(@Validated @ModelAttribute RetrieveReportRequest request) {

        List<String> breedList = parseBreeds(request.getBreeds());

        ReportCardDTO reportCardDTO = reportAnimalRetrieveService.retrieveReportCardsWithFilters(
                1L,
                request.getLastReportId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getSpecies(),
                breedList,
                request.getLocation());

        return new BaseResponse<>(reportCardDTO);
    }

    @Operation(summary = "전체 조회", description = "모든 동물들의 정보를 조회합니다.")
    @GetMapping
    public BaseResponse<TotalCardDTO> retrieveAll(@Validated @ModelAttribute RetrieveAllRequest request) {

        List<String> breedList = parseBreeds(request.getBreeds());

        TotalCardDTO totalCardDTO = animalRetrieveService.retrieveTotalCardsWithFilters(
                1L,
                request.getLastProtectId(),
                request.getLastReportId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getSpecies(),
                breedList,
                request.getLocation()
        );

        return new BaseResponse<>(totalCardDTO);
    }

    @Operation(
            summary = "실종 신고 게시글 등록",
            description = "실종 신고 게시글을 등록합니다."
    )
    @PostMapping("/new-missing-reports")
    public BaseResponse<Void> postMissingReport(@RequestBody MissingReportDTO requestDTO) {
        missingReportPostService.createReport(requestDTO);
        return new BaseResponse<>(null);
    }
    @Operation(
            summary = "목격 신고 게시글 등록",
            description = "목격 신고 게시글을 등록합니다."
    )
    @PostMapping("/new-witness-reports")
    public BaseResponse<Void> postWitnessReport(@RequestBody WitnessReportDTO requestDTO) {
        witnessReportPostService.createReport(requestDTO);
        return new BaseResponse<>(null);
    }
    @Operation(
            summary = "게시글 삭제",
            description = "목격 신고, 실종 신고 게시글을 삭제합니다."
    )
    @DeleteMapping("/reports/{report_id}")
    public BaseResponse<Void> deleteReport(@PathVariable("report_id") Long reportId) {
        reportDeleteService.deleteReport(reportId);
        return new BaseResponse<>(null);
    }

    // 문자열을 쉼표로 분할하고 각 요소의 앞뒤 공백을 제거하는 메서드
    private List<String> parseBreeds(String breeds) {
        if (breeds == null || breeds.isBlank()) {
            return null;
        }
        return Arrays.stream(breeds.split(","))
                .map(String::trim)   // 각 요소의 앞뒤 공백 제거
                .filter(s -> !s.isEmpty())  // 공백만 있는 요소 제거
                .toList();
    }

}
