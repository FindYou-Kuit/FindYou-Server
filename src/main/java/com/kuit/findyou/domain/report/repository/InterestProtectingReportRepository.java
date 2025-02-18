package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestProtectingReportRepository extends JpaRepository<InterestProtectingReport, Long> {
    boolean existsByUserIdAndProtectingReportId(Long userId, Long reportId);

    Slice<InterestProtectingReport> findAllByIdLessThanAndUserId(Long id, Long userId, Pageable pageable);

    List<InterestProtectingReport> findAllByUserId(Long userId);

    Optional<InterestProtectingReport> findByUserIdAndProtectingReportId(Long UserId, Long protectingReportId);
}
