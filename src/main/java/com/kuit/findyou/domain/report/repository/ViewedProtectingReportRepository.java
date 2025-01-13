package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewedProtectingReportRepository extends JpaRepository<ViewedProtectingReport, Long> {
}
