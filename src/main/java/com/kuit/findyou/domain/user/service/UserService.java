package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import com.kuit.findyou.domain.report.model.InterestReport;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.InterestReportRepository;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.exception.AlreadySavedInterestException;
import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final InterestProtectingReportRepository interestProtectingReportRepository;
    private final InterestReportRepository interestReportRepository;
    private final ProtectingReportRepository protectingReportRepository;
    private final ReportRepository reportRepository;
    public Long saveInterestAnimal(Long userId, PostInterestAnimalRequest request) {
        log.info("[saveInterestAnimal] request = {}", request);
        User user = findUser(userId);
        checkTagIsValid(request);
        if(isProtectingReport(request)){
            ProtectingReport protectingReport = findProtectingReport(request);
            if(alreadySavedInterestProtectingReport(user.getId(), protectingReport.getId())){
                throw new AlreadySavedInterestException(ALREADY_SAVED_INTEREST_REPORT);
            }
            return saveInterestProtectingReport(protectingReport, user);
        }
        Report report = findReport(request);
        if(alreadySavedInterestReport(user, report)){
            throw new AlreadySavedInterestException(ALREADY_SAVED_INTEREST_REPORT);
        }
        return saveInterestReport(report, user);
    }

    private Long saveInterestReport(Report report, User user) {
        InterestReport interestReport = InterestReport.createInterestReport(user, report);
        InterestReport saved = interestReportRepository.save(interestReport);
        return saved.getId();
    }

    private Long saveInterestProtectingReport(ProtectingReport protectingReport, User user) {
        InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
        InterestProtectingReport saved = interestProtectingReportRepository.save(interestProtectingReport);
        return saved.getId();
    }

    private void checkTagIsValid(PostInterestAnimalRequest request) {
        for(ReportTag tag : ReportTag.values()){
            if(request.getTag().equals(tag.getValue())){
                return;
            }
        }
        throw new BadRequestException(BAD_REQUEST);
    }

    private Report findReport(PostInterestAnimalRequest request) {
        Optional<Report> reportById = reportRepository.findById(request.getId());
        if(reportById.isEmpty()){
            throw new ReportNotFoundException(REPORT_NOT_FOUND);
        }
        return reportById.get();
    }

    private boolean alreadySavedInterestReport(User user, Report report) {
        return interestReportRepository.existsByUserAndReport(user, report);
    }

    private ProtectingReport findProtectingReport(PostInterestAnimalRequest request) {
        Optional<ProtectingReport> protectById = protectingReportRepository.findById(request.getId());
        if(protectById.isEmpty()){
            throw new ReportNotFoundException(REPORT_NOT_FOUND);
        }
        return protectById.get();
    }

    private User findUser(Long userId) {
        Optional<User> userById = userRepository.findById(userId);
        if(userById.isEmpty()){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        return userById.get();
    }

    private boolean alreadySavedInterestProtectingReport(Long userId, Long protectingReportId) {
        return interestProtectingReportRepository.existsByUserIdAndProtectingReportId(userId, protectingReportId);
    }

    private boolean isProtectingReport(PostInterestAnimalRequest request) {
        return request.getTag().equals(ReportTag.PROTECTING.getValue());
    }
}