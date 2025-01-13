package com.kuit.findyou.report.repository;

import com.kuit.findyou.domain.report.model.ReportAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportAnimalRepository extends JpaRepository<ReportAnimal, Long> {

    // fetch join - N + 1 문제를 해결하고싶을 때 정보를 한 번에 끌어올 수 있음
    @Query("SELECT ra FROM ReportAnimal ra " +
    "JOIN FETCH ra.breed " +
    "LEFT JOIN FETCH ra.reportedAnimalFeatures raf " +
    "LEFT JOIN FETCH raf.feature " +
    "WHERE ra.id = :reportAnimalId")
    ReportAnimal findWithBreedAndFeaturesById(@Param("reportAnimalId") Long reportAnimalId);
}
