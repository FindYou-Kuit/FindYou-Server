package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Slice<Report> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);


    @Query("SELECT r FROM Report r " +
    "WHERE r.id < :id " +
    "AND (:startDate IS NULL OR r.eventDate >= :startDate) " +
    "AND (:endDate IS NULL OR r.eventDate <= :endDate) " +
    "AND (:species IS NULL OR r.reportAnimal.breed.species = :species) " +
    "AND (:breeds IS NULL OR r.reportAnimal.breed.name IN :breeds) " +
    "AND (:location IS NULL OR r.foundLocation LIKE CONCAT('%', :location, '%'))" +
    "ORDER BY r.id DESC")
    Slice<Report> findReportsWithFilters(@Param("id") Long id,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         @Param("species") String species,
                                         @Param("breeds") List<String> breeds,
                                         @Param("location") String location,
                                         Pageable pageable);

    Long countByCreatedAtEquals(LocalDateTime datetime);

    List<Report> findTop10ByOrderByCreatedAtDesc();

}
