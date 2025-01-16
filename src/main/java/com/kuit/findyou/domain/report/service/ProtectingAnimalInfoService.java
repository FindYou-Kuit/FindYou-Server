package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ViewedProtectingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProtectingAnimalInfoService {

    private final ProtectingReportRepository protectingReportRepository;
    private final ViewedProtectingReportRepository viewedProtectingReportRepository;
    private final UserRepository userRepository;


    @Transactional
    public ProtectingReportInfoDTO findProtectingReportInfoById(Long protectingReportId, Long userId) {
        ProtectingReport protectingReport = protectingReportRepository.findById(protectingReportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보호글입니다."));   // 커스텀 예외로 바꿀수도

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));    // 커스텀 예외로 바꿀수도




        // 마지막에 본 것만 등록되도록 하기 위해, 기존에 동일한 보호글을 본 적이 있다면, 해당 정보를 삭제
        viewedProtectingReportRepository.findByUserAndProtectingReport(loginedUser, protectingReport)
                .ifPresent(existingReport -> {
                    // User의 컬렉션에서 제거
                    loginedUser.removeViewedProtectingReport(existingReport);

                    // Soft delete 수행
                    viewedProtectingReportRepository.delete(existingReport);
                });


        // 최근 본 보호글로 등록
        ViewedProtectingReport viewedProtectingReport = ViewedProtectingReport.createViewedProtectingReport(loginedUser, protectingReport);
        viewedProtectingReportRepository.save(viewedProtectingReport);


        return ProtectingReportInfoDTO.newInstanceFromProtectingReportWithUser(protectingReport, loginedUser);
    }
}
