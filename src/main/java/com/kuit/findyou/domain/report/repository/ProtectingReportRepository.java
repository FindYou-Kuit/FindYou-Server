package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.ProtectingReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProtectingReportRepository extends JpaRepository<ProtectingReport, Long> {


    Slice<ProtectingReport> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    @Query("SELECT pr FROM ProtectingReport pr " +
            "WHERE pr.id < :id " +
            "AND (:startDate IS NULL OR pr.happenDate >= :startDate) " +
            "AND (:endDate IS NULL OR pr.happenDate <= :endDate) " +
            "AND (:species IS NULL OR pr.species = :species) " +
            "AND (:breeds IS NULL OR pr.breed IN :breeds) " +
            "AND (:location IS NULL OR pr.authority LIKE CONCAT('%', :location, '%'))" +
            "ORDER BY pr.id DESC")
    Slice<ProtectingReport> findProtectingReportWithFilters(@Param("id") Long id,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate,
                                                            @Param("species") String species,
                                                            @Param("breeds") List<String> breeds,
                                                            @Param("location") String location,
                                                            Pageable pageable);

    Long countByNoticeStartDateEquals(LocalDate date);

    List<ProtectingReport> findTop10ByOrderByCreatedAtDesc();
}
