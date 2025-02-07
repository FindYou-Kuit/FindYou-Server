package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.report.model.Report;
import com.kuit.findyou.domain.report.model.ViewedReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewedReportRepository extends JpaRepository<ViewedReport, Long> {

    // 특정 유저와 신고글을 기준으로 ViewedReport 조회
    Optional<ViewedReport> findByUserAndReport(User user, Report report);

    Slice<ViewedReport> findByUserAndIdLessThanOrderByIdDesc(User user, Long lastViewedReportId, Pageable pageable);
}
