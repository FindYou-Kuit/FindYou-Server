package com.kuit.findyou.domain.report.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kuit.findyou.domain.report.exception.FileStorageException;
import com.kuit.findyou.domain.report.model.Image;
import com.kuit.findyou.domain.report.repository.ImageRepository;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final AmazonS3 s3client;
    private final String bucketName = "findyoubucket";

    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        List<String> imageKeys = new ArrayList<>();
        List<String> imageUrls = new ArrayList<>();


        for (MultipartFile file : files) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString();
            String savedFileName = uuid + fileExtension;


            // S3에 파일 업로드
            try {
                s3client
                        .putObject(new PutObjectRequest(bucketName, savedFileName, file.getInputStream(), null)
                                .withCannedAcl(CannedAccessControlList.PublicRead)); //공개 url 생성 (공개적으로 접근 가능하도록 설정)
                String imageUrl = s3client.getUrl(bucketName, savedFileName).toString();

                // 이미지 메타데이터 저장
                Image image = Image.createImage(imageUrl, uuid);
                imageRepository.save(image);
                imageUrls.add(imageUrl);
                imageKeys.add(uuid);
            } catch (Exception e) {
                throw new FileStorageException(BaseExceptionResponseStatus.UPLOAD_ERROR);
            }
        }
        return imageUrls;
    }

    /*private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }*/
    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없으면 빈 문자열 반환
        }
        return fileName.substring(lastIndexOfDot);
    }
}
