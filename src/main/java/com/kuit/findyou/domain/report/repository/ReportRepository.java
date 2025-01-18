package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Long countByCreatedAtEquals(LocalDateTime datetime);

    List<Report> findTop10ByOrderByCreatedAtDesc();
}
