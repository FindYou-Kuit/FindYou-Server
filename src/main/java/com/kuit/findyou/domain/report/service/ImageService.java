package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.report.model.Image;
import com.kuit.findyou.domain.report.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        List<String> imageKeys = new ArrayList<>();

        Path directory = Paths.get("uploads");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        for (MultipartFile file : files) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString();

            String savedFileName = uuid + fileExtension;
            Path path = Paths.get(directory.toString() + savedFileName);
            Files.copy(file.getInputStream(), path);

            //이미지 메타데이터 저장
            Image image = Image.createImage(path.toString(), uuid);
            imageRepository.save(image);
            imageKeys.add(uuid);
        }
        return imageKeys;
    }
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
