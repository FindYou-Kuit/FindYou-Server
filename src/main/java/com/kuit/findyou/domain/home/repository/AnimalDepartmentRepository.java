package com.kuit.findyou.domain.home.repository;

import com.kuit.findyou.domain.home.model.AnimalDepartment;
import com.kuit.findyou.domain.home.model.AnimalShelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalDepartmentRepository extends JpaRepository<AnimalDepartment, Long> {
}
