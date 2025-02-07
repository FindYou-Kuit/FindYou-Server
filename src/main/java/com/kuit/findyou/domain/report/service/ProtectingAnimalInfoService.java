package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ViewedProtectingReportRepository;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.REPORT_NOT_FOUND;
import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProtectingAnimalInfoService {

    private final ProtectingReportRepository protectingReportRepository;
    private final ViewedProtectingReportRepository viewedProtectingReportRepository;
    private final UserRepository userRepository;


    @Transactional
    public ProtectingReportInfoDTO findProtectingReportInfoById(Long protectingReportId, Long userId) {
        ProtectingReport protectingReport = protectingReportRepository.findById(protectingReportId)
                .orElseThrow(() -> new ReportNotFoundException(REPORT_NOT_FOUND));

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));


        // 마지막에 본 것만 등록되도록 하기 위해, 기존에 동일한 보호글을 본 적이 있다면, 해당 정보를 삭제
        viewedProtectingReportRepository.findByUserAndProtectingReport(loginedUser, protectingReport)
                .ifPresent(viewedProtectingReportRepository::delete); // soft delete 수행


        // 최근 본 보호글로 등록
        ViewedProtectingReport viewedProtectingReport = ViewedProtectingReport.createViewedProtectingReport(loginedUser, protectingReport);
        viewedProtectingReportRepository.save(viewedProtectingReport);


        return ProtectingReportInfoDTO.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser);
    }
}
