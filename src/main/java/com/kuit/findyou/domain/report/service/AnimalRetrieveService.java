package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.response.TotalCardDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnimalRetrieveService {

    private final UserRepository userRepository;
    private final ProtectingReportRepository protectingReportRepository;
    private final ReportRepository reportRepository;

    public TotalCardDTO retrieveTotalCardsWithFilters(
            Long userId,
            Long lastProtectId,
            Long lastReportId,
            LocalDate startDate,
            LocalDate endDate,
            String species,
            List<String> breeds,
            String location) {

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Slice<ProtectingReport> protectingReportSlice = protectingReportRepository.findProtectingReportWithFilters(lastProtectId,startDate, endDate, species, breeds, location, PageRequest.of(0, 20));
        List<ProtectingReport> protectingReportList = protectingReportSlice.getContent();

        Slice<Report> reportSlice = reportRepository.findReportsWithFilters(lastReportId,startDate, endDate, species, breeds, location, PageRequest.of(0, 20));
        List<Report> reportList = reportSlice.getContent();

        return TotalCardDTO.mergeCards(protectingReportList, reportList, loginedUser);
    }


}
