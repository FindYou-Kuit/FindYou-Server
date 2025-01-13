package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
}
