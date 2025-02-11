package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.report.exception.ReportCreationException;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportDeleteService {
    private final ReportRepository reportRepository;


    public void deleteReport(Long reportId) throws ReportNotFoundException {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.REPORT_NOT_FOUND));

        reportRepository.delete(report);


    }
}
