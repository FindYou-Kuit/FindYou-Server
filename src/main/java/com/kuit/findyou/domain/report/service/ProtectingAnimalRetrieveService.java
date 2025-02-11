package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ProtectingReportCardDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
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
public class ProtectingAnimalRetrieveService {

    private final ProtectingReportRepository protectingReportRepository;
    private final UserRepository userRepository;

    public ProtectingReportCardDTO retrieveProtectingReportCardsWithFilters(
            Long userId,
            Long lastProtectId,
            LocalDate startDate,
            LocalDate endDate,
            String species,
            List<String> breeds,
            String location) {

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Slice<ProtectingReport> protectingReportSlice = protectingReportRepository.findProtectingReportWithFilters(lastProtectId, startDate, endDate, species, breeds, location, PageRequest.of(0, 20));

        List<Card> cards = protectingReportSlice.map(protectingReport ->
                        Card.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser))
                .getContent();

        Boolean isLast = protectingReportSlice.isLast();

        Long newLastProtectId = -1L;
        if (!cards.isEmpty()) {
            newLastProtectId = cards.get(cards.size() - 1).getCardId();
        }

        return ProtectingReportCardDTO.newInstance(cards, newLastProtectId, isLast);
    }
}
