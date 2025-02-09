package com.kuit.findyou.domain.image.repository;

import com.kuit.findyou.domain.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByFilePath(String filePath);
}
