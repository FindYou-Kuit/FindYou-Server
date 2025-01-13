package com.kuit.findyou.report.repository;

import com.kuit.findyou.domain.report.model.ViewedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewedReportRepository extends JpaRepository<ViewedReport, Long> {
}
