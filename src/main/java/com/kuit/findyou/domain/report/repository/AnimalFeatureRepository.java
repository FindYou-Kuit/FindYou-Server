package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.AnimalFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalFeatureRepository extends JpaRepository<AnimalFeature, Long> {
}
