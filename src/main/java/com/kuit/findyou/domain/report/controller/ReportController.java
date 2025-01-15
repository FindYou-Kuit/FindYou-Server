package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.dto.ReportInfoDTO;
import com.kuit.findyou.domain.report.repository.*;
import com.kuit.findyou.domain.report.service.ProtectingAnimalInfoService;
import com.kuit.findyou.domain.report.service.ReportAnimalInfoService;
import com.kuit.findyou.global.common.response.BaseResponse;
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
}
