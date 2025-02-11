package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ReportInfoDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.REPORT_NOT_FOUND;
import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAnimalInfoService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ViewedReportRepository viewedReportRepository;

    public ReportInfoDTO findReportInfoById(Long reportId, Long userId) {

        // 인자로 넘어온 id 에 맞는 신고글 정보
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(REPORT_NOT_FOUND));  // 커스텀 예외로 바꿀수도


        // 현재 로그인중인 유저 정보
        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));    // 커스텀 예외로 바꿀수도


        // 마지막에 본 것만 등록되도록 하기 위해, 기존에 동일한 보호글을 본 적이 있다면, 해당 정보를 삭제
        viewedReportRepository.findByUserAndReport(loginedUser, report)
                .ifPresent(viewedReportRepository::delete);

        // 최근 본 신고글로 등록
        ViewedReport viewedReport = ViewedReport.createViewedReport(loginedUser, report);
        viewedReportRepository.save(viewedReport);


        return ReportInfoDTO.newInstanceFromReportWithUser(report, loginedUser);
    }
}
