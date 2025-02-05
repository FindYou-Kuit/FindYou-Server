package com.kuit.findyou.domain.report.repository;

import com.kuit.findyou.domain.report.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByFilePath(String filePath);
}
