package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.report.model.InterestReport;
import com.kuit.findyou.domain.report.model.Report;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestReportRepository extends JpaRepository<InterestReport, Long> {
    boolean existsByUserAndReport(User user, Report report);

    Slice<InterestReport> findAllByIdLessThanAndUserId(Long lastProtectId, Long userId, PageRequest of);

    Optional<InterestReport> findByUserIdAndReportId(Long userId, Long reportId);
}
