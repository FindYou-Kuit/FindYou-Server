package com.kuit.findyou.domain.report.service;
import com.kuit.findyou.domain.report.model.Image;
import com.kuit.findyou.domain.report.repository.ImageRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        imageRepository.deleteAll(); // 데이터베이스 내의 모든 이미지 정보를 삭제
    }


    @Test
    void testUploadMultipleImagesFromFileSystem() throws IOException {
        // 로컬에 있는 사진으로 테스트
        String[] imagePaths = {
                "C:/images/cloud/1.jpg",
                "C:/images/cloud/2.jpg",
                "C:/images/cloud/3.jpg"
        };

        List<MultipartFile> multipartFiles = new ArrayList<>();

        // 각 파일에 대한 MockMultipartFile 객체 생성
        for (String path : imagePaths) {
            File file = new File(path);
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/jpg", IOUtils.toByteArray(input));
            multipartFiles.add(multipartFile);
            input.close();
        }

        List<String> imageKeys = imageService.saveImages(multipartFiles);

        // 개수 일치하는지 학인
        assertEquals(imagePaths.length, imageKeys.size());

        // DB 확인
        List<Image> savedImages = imageRepository.findAll();
        assertEquals(imagePaths.length, savedImages.size());

        // 테스트 후 DB 정리
        savedImages.forEach(image -> imageRepository.delete(image));
    }

}
