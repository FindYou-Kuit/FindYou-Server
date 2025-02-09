package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewedProtectingReportRepository extends JpaRepository<ViewedProtectingReport, Long> {

    // 특정 유저와 보호글을 기준으로 ViewedProtectingReport 조회
    Optional<ViewedProtectingReport> findByUserAndProtectingReport(User user, ProtectingReport protectingReport);

    Slice<ViewedProtectingReport> findByUserAndIdLessThanOrderByIdDesc(User user, Long lastViewedProtectingReportId, Pageable pageable);
}


