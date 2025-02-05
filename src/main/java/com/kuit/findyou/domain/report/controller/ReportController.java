package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.report.dto.*;
import com.kuit.findyou.domain.report.service.*;
import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;

import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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


    @GetMapping("/report-animals/{report_id}")
    public BaseResponse<ReportInfoDTO> reportInfo(@PathVariable("report_id") Long reportId) {
        ReportInfoDTO findReportInfo = reportAnimalInfoService.findReportInfoById(reportId, 1L);

        return new BaseResponse<>(findReportInfo);
    }

    @GetMapping("/protecting-animals/{protecting_report_id}")
    public BaseResponse<ProtectingReportInfoDTO> protectingReportInfo(@PathVariable("protecting_report_id") Long protectingReportId) {
        ProtectingReportInfoDTO findProtectingInfo = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, 1L);

        return new BaseResponse<>(findProtectingInfo);
    }

    @GetMapping("/protecting-animals")
    public BaseResponse<ProtectingReportCardDTO> retrieveProtectingReports(
            @RequestParam("lastProtectId") Long lastProtectId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "breeds", required = false) List<String> breeds,
            @RequestParam(value = "location", required = false) String location) {
        ProtectingReportCardDTO protectingReportCardDTO =
                protectingAnimalRetrieveService.retrieveProtectingReportCardsWithFilters(1L, lastProtectId, startDate, endDate, species, breeds, location);

        return new BaseResponse<>(protectingReportCardDTO);
    }

    @GetMapping("/report-animals")
    public BaseResponse<ReportCardDTO> retrieveReports(
            @RequestParam("lastReportId") Long lastReportId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "breeds", required = false) List<String> breeds,
            @RequestParam(value = "location", required = false) String location) {
        ReportCardDTO reportCardDTO = reportAnimalRetrieveService.retrieveReportCardsWithFilters(1L, lastReportId, startDate, endDate, species, breeds, location);

        return new BaseResponse<>(reportCardDTO);
    }

    @GetMapping
    public BaseResponse<TotalCardDTO> retrieveAll(
            @RequestParam("lastProtectId") Long lastProtectId,
            @RequestParam("lastReportId") Long lastReportId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "breeds", required = false) List<String> breeds,
            @RequestParam(value = "location", required = false) String location) {
        TotalCardDTO totalCardDTO = animalRetrieveService.retrieveTotalCardsWithFilters(1L, lastProtectId, lastReportId, startDate, endDate, species, breeds, location);

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


}
