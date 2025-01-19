package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.report.dto.ImageUploadDTO;
import com.kuit.findyou.domain.report.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<List<String>> uploadImages(@ModelAttribute ImageUploadDTO imageUploadDTO) {
        try {
            List<String> imageKeys = imageService.saveImages(imageUploadDTO.getFiles());
            return ResponseEntity.ok(imageKeys);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
