package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.report.model.Image;
import com.kuit.findyou.domain.report.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    // 생성자 주입 방식
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        List<String> imageKeys = new ArrayList<>();

        Path directory = Paths.get("uploads");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        for (MultipartFile file : files) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString();
            if (uuid == null || uuid.isEmpty()) {
                throw new IllegalStateException("UUID cannot be null");
            }
            String savedFileName = uuid + fileExtension;
            Path path = Paths.get(directory.toString() + savedFileName);
            Files.copy(file.getInputStream(), path);

            //이미지 메타데이터 저장
            Image image = new Image();
            //image.setFileName(savedFileName);
            image.setFilePath(path.toString());
            image.setImageKey(uuid); // UUID를 이미지 객체에 설정
            imageRepository.save(image);

            imageKeys.add(uuid);
        }
        return imageKeys;
    }
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
