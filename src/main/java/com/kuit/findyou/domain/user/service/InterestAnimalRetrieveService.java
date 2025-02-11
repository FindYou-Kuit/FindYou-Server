package com.kuit.findyou.domain.user.service;

import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import com.kuit.findyou.domain.report.model.InterestReport;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.InterestReportRepository;
import com.kuit.findyou.domain.user.dto.response.GetInterestAnimalCursorPageDto;
import com.kuit.findyou.domain.user.dto.response.InterestAnimalCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterestAnimalRetrieveService {
    private final UserRepository userRepository;
    private final InterestProtectingReportRepository interestProtectingReportRepository;
    private final InterestReportRepository interestReportRepository;

    public GetInterestAnimalCursorPageDto getInterestAnimalCursorPage(Long userId, Long lastInterestReportId, Long lastInterestProtectId, int size) {
        // 아래 코드는 추후에 JWT 사용시에 userId를 얻기 위해서 필요함
        // User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        Slice<InterestProtectingReport> interestProtectSlice = interestProtectingReportRepository.findAllByIdLessThanAndUserId(lastInterestProtectId, userId, PageRequest.of(0, size, Sort.by("id").descending()));
        Slice<InterestReport> interestReportSlice = interestReportRepository.findAllByIdLessThanAndUserId(lastInterestReportId, userId, PageRequest.of(0, size, Sort.by("id").descending()));
        log.info("[getInterestAnimalCursorPage] protectSlice length = {}, interestSlice length = {}", interestProtectSlice.getContent().size(), interestReportSlice.getContent().size());
        return makeCursorPage(interestProtectSlice, interestReportSlice, size, lastInterestProtectId, lastInterestReportId);
    }

    private GetInterestAnimalCursorPageDto makeCursorPage(Slice<InterestProtectingReport> interestProtectSlice, Slice<InterestReport> interestReportSlice, int size, Long lastInterestProtectId, Long lastInterestReportId) {
        List<InterestReport> interestReports = interestReportSlice.getContent();
        List<InterestProtectingReport> interestProtects = interestProtectSlice.getContent();
        List<InterestAnimalCard> result = new ArrayList<>();
        int ipIndex = 0;
        int irIndex = 0;
        InterestProtectingReport interestProtect = null;
        InterestReport interestReport = null;
        while(ipIndex < interestProtects.size() && irIndex < interestReports.size() && result.size() < size){
            interestProtect = interestProtects.get(ipIndex);
            interestReport = interestReports.get(irIndex);
            if(interestProtect.getCreatedAt().isAfter(interestReport.getCreatedAt())){
                result.add(InterestAnimalCard.from(interestProtect));
                lastInterestProtectId = interestProtect.getId();
                ipIndex++;
            }
            else{
                result.add(InterestAnimalCard.from(interestReport));
                lastInterestReportId = interestReport.getId();
                irIndex++;
            }
        }
        while(ipIndex < interestProtects.size() && result.size() < size){
            interestProtect = interestProtects.get(ipIndex);
            result.add(InterestAnimalCard.from(interestProtect));
            lastInterestProtectId = interestProtect.getId();
            ipIndex++;
        }
        while(irIndex < interestReports.size() && result.size() < size){
            interestReport = interestReports.get(irIndex);
            result.add(InterestAnimalCard.from(interestReport));
            lastInterestReportId = interestReport.getId();
            irIndex++;
        }
        return GetInterestAnimalCursorPageDto.builder()
                .interestAnimals(result)
                .lastInterestProtectId(lastInterestProtectId)
                .lastInterestReportId(lastInterestReportId)
                .isLast(interestProtects.size() + interestReports.size() < size)
                .build();
    }
}
