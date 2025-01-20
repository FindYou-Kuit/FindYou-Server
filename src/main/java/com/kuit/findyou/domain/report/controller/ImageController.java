package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.report.dto.ImageUploadDTO;
import com.kuit.findyou.domain.report.service.ImageService;
import com.kuit.findyou.global.common.exception.FileStorageException;
import com.kuit.findyou.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
    public BaseResponse<List<String>> uploadImages(@ModelAttribute ImageUploadDTO imageUploadDTO) {
        try {
            List<String> imageKeys = imageService.saveImages(imageUploadDTO.getFiles());
            return new BaseResponse<>(imageKeys);
        } catch (IOException e) {
            throw new FileStorageException("파일 업로드 중 에러가 발생했습니다",e);
        }
    }
}
