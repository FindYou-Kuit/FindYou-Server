package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ProtectingReportCardDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.repository.InterestProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProtectingAnimalRetrieveService {

    private final ProtectingReportRepository protectingReportRepository;
    private final UserRepository userRepository;

    public ProtectingReportCardDTO retrieveProtectingReportCards(Long userId, Long lastProtectId) {
        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Slice<ProtectingReport> protectingReportSlice = protectingReportRepository.findByIdLessThanOrderByIdDesc(lastProtectId, PageRequest.of(0, 20));

        List<Card> cards = protectingReportSlice.map(protectingReport -> Card.builder()
                        .id(protectingReport.getId())
                        .thumbnailImageUrl(protectingReport.getImageUrl())
                        .title(protectingReport.getBreed())
                        .tag("보호중")
                        .date(protectingReport.getHappenDate().toString())
                        .location(protectingReport.getCareAddr())
                        .interest(loginedUser.isInterestProtectingReport(protectingReport.getId()))
                        .build())
                        .getContent();

        Boolean isLast = false;

        if (protectingReportSlice.isLast()) {
            isLast = true;
        }

        Long newLastProtectId = cards.get(cards.size() - 1).getId();

        return ProtectingReportCardDTO.builder()
                .cards(cards)
                .lastProtectId(newLastProtectId)
                .isLast(isLast)
                .build();
    }
}
