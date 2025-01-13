package com.kuit.findyou.report.repository;

import com.kuit.findyou.domain.report.model.InterestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestReportRepository extends JpaRepository<InterestReport, Long> {
}
