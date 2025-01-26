package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.TotalCardDTO;
import com.kuit.findyou.domain.report.dto.ViewedReportCardDTO;
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

    public ViewedReportCardDTO retrieveAllViewedReports(Long userId, Long lastViewedProtectId, Long lastViewedReportId) {
        User loginedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Slice<ViewedProtectingReport> viewedProtectingReportSlice = viewedProtectingReportRepository.findByUserAndIdLessThanOrderByIdDesc(loginedUser, lastViewedProtectId, PageRequest.of(0, 20));
        Slice<ViewedReport> viewedReportSlice = viewedReportRepository.findByUserAndIdLessThanOrderByIdDesc(loginedUser, lastViewedReportId, PageRequest.of(0, 20));

        List<ViewedProtectingReport> viewedProtectingReportList = viewedProtectingReportSlice.getContent();
        List<ViewedReport> viewedReportList = viewedReportSlice.getContent();

        return ViewedReportCardDTO.mergeCards(viewedProtectingReportList, viewedReportList, loginedUser);
    }
}
