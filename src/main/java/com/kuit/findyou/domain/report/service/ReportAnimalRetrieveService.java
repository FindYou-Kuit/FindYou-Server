package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.Card;
import com.kuit.findyou.domain.report.dto.ReportCardDTO;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAnimalRetrieveService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportCardDTO retrieveReportCards(Long userId, Long lastReportId) {
        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Slice<Report> reportSlice = reportRepository.findByIdLessThanOrderByIdDesc(lastReportId, PageRequest.of(0, 20));

        List<Card> cards = reportSlice.map(report -> Card.builder()
                        .id(report.getId())
                        .thumbnailImageUrl("1")   // image 관련 로직이 아직 없어서 임시로 넣은 데이터
                        .title(report.getReportAnimal().getBreed().getName())
                        .tag(report.getTag())
                        .date(report.getEventDate().toString())
                        .location(report.getFoundLocation())
                        .interest(loginedUser.isInterestReport(report.getId()))
                        .build())
                .getContent();

        Boolean isLast = false;

        if (reportSlice.isLast()) {
            isLast = true;
        }

        Long newLastReportId = -1L;
        if(!cards.isEmpty()) {
            newLastReportId = cards.get(cards.size() - 1).getId();
        }

        return ReportCardDTO.builder()
                .cards(cards)
                .lastReportId(newLastReportId)
                .isLast(isLast)
                .build();
    }
}
