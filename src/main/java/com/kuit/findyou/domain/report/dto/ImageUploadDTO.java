package com.kuit.findyou.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@Getter
@Builder
public class ImageUploadDTO {
    //이미지 여러개 업로드 가능하도록
    private List<MultipartFile> files;
    public static ImageUploadDTO createImageUploadDTO(List<MultipartFile> files) {
        return ImageUploadDTO.builder()
                .files(files)
                .build();
    }
}
