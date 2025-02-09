package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedReport;
import com.kuit.findyou.domain.report.repository.ViewedProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ViewedReportRepository;
import com.kuit.findyou.domain.user.dto.ViewedCardDTO;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ViewedAnimalRetrieveService {

    private final UserRepository userRepository;
    private final ViewedProtectingReportRepository viewedProtectingReportRepository;
    private final ViewedReportRepository viewedReportRepository;

    public ViewedCardDTO retrieveAllViewedReports(Long userId, Long lastViewedProtectId, Long lastViewedReportId) {
        User loginedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Slice<ViewedProtectingReport> viewedProtectingReportSlice = viewedProtectingReportRepository.findByUserAndIdLessThanOrderByIdDesc(loginedUser, lastViewedProtectId, PageRequest.of(0, 20));
        Slice<ViewedReport> viewedReportSlice = viewedReportRepository.findByUserAndIdLessThanOrderByIdDesc(loginedUser, lastViewedReportId, PageRequest.of(0, 20));

        List<ViewedProtectingReport> viewedProtectingReportList = viewedProtectingReportSlice.getContent();
        List<ViewedReport> viewedReportList = viewedReportSlice.getContent();

        return ViewedCardDTO.mergeCards(viewedProtectingReportList, viewedReportList, loginedUser);
    }
}
