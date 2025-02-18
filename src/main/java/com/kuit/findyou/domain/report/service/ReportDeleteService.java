package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.report.exception.ReportCreationException;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.user.repository.UserRepository;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportDeleteService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public void deleteReport(Long reportId, Long userId) throws ReportNotFoundException {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.REPORT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(BaseExceptionResponseStatus.USER_NOT_FOUND));

        //토큰과 일치하는지 확인
        if(!report.getUser().getId().equals(userId)) {
            throw new UserNotFoundException(BaseExceptionResponseStatus.UNAUTHORIZED_USER_ID);
        }
        reportRepository.delete(report);
    }
}
