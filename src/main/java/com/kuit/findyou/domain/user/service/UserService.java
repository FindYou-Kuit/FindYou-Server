package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
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
import com.kuit.findyou.domain.user.exception.InterestAnimalNotFoundException;
import com.kuit.findyou.global.common.exception.UnauthorizedUserException;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public Long saveInterestProtectingAnimal(Long userId, PostInterestAnimalRequest request) {
        log.info("[saveInterestProtectingAnimal] request = {}", request);
        User user = findUser(userId);
        ProtectingReport protectingReport = findProtectingReport(request);
        if(alreadySavedInterestProtectingReport(user.getId(), protectingReport.getId())){
            throw new AlreadySavedInterestException(ALREADY_SAVED_INTEREST_REPORT);
        }
        InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
        InterestProtectingReport saved = interestProtectingReportRepository.save(interestProtectingReport);
        return saved.getId();
    }

    public Long saveInterestReportAnimal(Long userId, PostInterestAnimalRequest request){
        log.info("[saveInterestReportAnimal] request = {}", request);
        User user = findUser(userId);
        Report report = findReport(request);
        if(alreadySavedInterestReport(user, report)){
            throw new AlreadySavedInterestException(ALREADY_SAVED_INTEREST_REPORT);
        }
        InterestReport interestReport = InterestReport.createInterestReport(user, report);
        InterestReport saved = interestReportRepository.save(interestReport);
        return saved.getId();
    }

    private Report findReport(PostInterestAnimalRequest request) {
        return reportRepository.findById(request.getId()).orElseThrow(() -> new ReportNotFoundException(REPORT_NOT_FOUND));
    }

    private boolean alreadySavedInterestReport(User user, Report report) {
        return interestReportRepository.existsByUserAndReport(user, report);
    }

    private ProtectingReport findProtectingReport(PostInterestAnimalRequest request) {
        return protectingReportRepository.findById(request.getId()).orElseThrow(()-> new ReportNotFoundException(REPORT_NOT_FOUND));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND));
    }

    private boolean alreadySavedInterestProtectingReport(Long userId, Long protectingReportId) {
        return interestProtectingReportRepository.existsByUserIdAndProtectingReportId(userId, protectingReportId);
    }

    public void removeInterestProtectingAnimal(Long userId, Long interestId) {
        InterestProtectingReport interest = interestProtectingReportRepository.findById(interestId).orElseThrow(() -> new InterestAnimalNotFoundException(INTEREST_ANIMAL_NOT_FOUND));
        if (interest.getUser().getId() != userId){
            throw new UnauthorizedUserException(UNATHORIZED_USER);
        }
        interestProtectingReportRepository.delete(interest);
    }

    public void removeInterestReportAnimal(Long userId, Long interestId) {
        InterestReport interest = interestReportRepository.findById(interestId).orElseThrow(() -> new InterestAnimalNotFoundException(INTEREST_ANIMAL_NOT_FOUND));
        if(interest.getUser().getId() != userId){
            throw new UnauthorizedUserException(UNATHORIZED_USER);
        }
        interestReportRepository.delete(interest);
    }
}