package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.report.model.InterestReport;
import com.kuit.findyou.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestReportRepository extends JpaRepository<InterestReport, Long> {
    boolean existsByUserAndReport(User user, Report report);
}
