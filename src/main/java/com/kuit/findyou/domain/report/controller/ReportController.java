package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.dto.ReportInfoDTO;
import com.kuit.findyou.domain.report.repository.*;
import com.kuit.findyou.domain.report.service.ProtectingAnimalInfoService;
import com.kuit.findyou.domain.report.service.ReportAnimalInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportAnimalInfoService reportAnimalInfoService;
    private final ProtectingAnimalInfoService protectingAnimalInfoService;

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    private final BreedRepository breedRepository;
    private final ReportAnimalRepository reportAnimalRepository;
    private final AnimalFeatureRepository animalFeatureRepository;
    private final ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;
    private final InterestReportRepository interestReportRepository;


    @GetMapping("/report-animals/{report_id}")
    public ResponseEntity<ReportInfoDTO> reportInfo(@PathVariable("report_id") Long reportId) {
        ReportInfoDTO findReportInfo = reportAnimalInfoService.findReportInfoById(reportId, 1L);

        return ResponseEntity.ok(findReportInfo);
    }

    @GetMapping("/protecting-animals/{protecting_report_id}")
    public ResponseEntity<ProtectingReportInfoDTO> protectingReportInfo(@PathVariable("protecting_report_id") Long protectingReportId) {
        ProtectingReportInfoDTO findProtectingInfo = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, 1L);

        return ResponseEntity.ok(findProtectingInfo);
    }
}
