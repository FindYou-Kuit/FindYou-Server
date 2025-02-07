package com.kuit.findyou.domain.image.controller;

import com.kuit.findyou.domain.image.dto.ImageUploadDTO;
import com.kuit.findyou.domain.image.service.ImageService;
import com.kuit.findyou.domain.image.exception.FileStorageException;
import com.kuit.findyou.global.common.response.BaseResponse;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
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
            if (imageUploadDTO.getFiles() == null || imageUploadDTO.getFiles().isEmpty()) {
                // 파일이 없는 경우 예외 처리
                throw new FileStorageException(BaseExceptionResponseStatus.NO_FILE_UPLOADED);
            }
            List<String> imageKeys = imageService.saveImages(imageUploadDTO.getFiles());
            return new BaseResponse<>(imageKeys);
        } catch (IOException e) {
            throw new FileStorageException(BaseExceptionResponseStatus.UPLOAD_ERROR);

        }
    }
}
