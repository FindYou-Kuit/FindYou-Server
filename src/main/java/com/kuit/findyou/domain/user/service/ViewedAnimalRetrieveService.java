package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.TotalCardDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedReport;
import com.kuit.findyou.domain.report.repository.ViewedProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ViewedReportRepository;
import com.kuit.findyou.domain.report.service.AnimalRetrieveService;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ViewedAnimalRetrieveService {

    private final UserRepository userRepository;
    private final ViewedProtectingReportRepository viewedProtectingReportRepository;
    private final ViewedReportRepository viewedReportRepository;

    public TotalCardDTO retrieveAllViewedReports(Long userId, Long lastProtectId, Long lastReportId) {
        User loginedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Slice<ViewedProtectingReport> viewedProtectingReportSlice = viewedProtectingReportRepository.findByUserAndIdLessThanOrderByIdDesc(loginedUser, lastProtectId, PageRequest.of(0, 20));
        Slice<ViewedReport> viewedReportSlice = viewedReportRepository.findByUserAndIdLessThanOrderByIdDesc(loginedUser, lastReportId, PageRequest.of(0, 20));

        List<ProtectingReport> protectingReportList = extractProtectingReports(viewedProtectingReportSlice.getContent());
        List<Report> reportList = extractReports(viewedReportSlice.getContent());

        return TotalCardDTO.mergeCards(protectingReportList, reportList, loginedUser);
    }

    private List<ProtectingReport> extractProtectingReports(List<ViewedProtectingReport> viewedProtectingReportList) {
        // ViewedProtectingReport에서 ProtectingReport 추출
        return viewedProtectingReportList.stream()
                .map(ViewedProtectingReport::getProtectingReport) // ProtectingReport 추출
                .collect(Collectors.toList()); // 리스트로 수집
    }

    private List<Report> extractReports(List<ViewedReport> viewedReportList) {
        // ViewedReport에서 Report 추출
        return viewedReportList.stream()
                .map(ViewedReport::getReport) // Report 추출
                .collect(Collectors.toList()); // 리스트로 수집
    }
}
