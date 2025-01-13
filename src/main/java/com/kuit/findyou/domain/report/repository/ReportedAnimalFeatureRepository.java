package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.ReportedAnimalFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedAnimalFeatureRepository extends JpaRepository<ReportedAnimalFeature, Long> {
}
