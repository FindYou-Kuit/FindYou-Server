package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.InterestProtectingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestProtectingReportRepository extends JpaRepository<InterestProtectingReport, Long> {
    boolean existsByUserIdAndProtectingReportId(Long userId, Long reportId);
}
