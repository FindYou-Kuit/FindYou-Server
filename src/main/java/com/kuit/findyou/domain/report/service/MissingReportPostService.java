package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.MissingReportDTO;
import com.kuit.findyou.domain.report.exception.ReportCreationException;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.AnimalFeatureRepository;
import com.kuit.findyou.domain.report.repository.BreedRepository;
import com.kuit.findyou.domain.report.repository.ImageRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissingReportPostService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final BreedRepository breedRepository;
    private final AnimalFeatureRepository animalFeatureRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void createReport(MissingReportDTO requestDTO) throws ReportCreationException {
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.USER_NOT_FOUND));
        Breed breed = breedRepository.findById(requestDTO.getBreedId()).orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.BREED_NOT_FOUND));
        List<AnimalFeature> features = animalFeatureRepository.findAllById(requestDTO.getFeatureIds());


        // ReportAnimal 객체 생성
        ReportAnimal reportAnimal = ReportAnimal.builder()
                .breed(breed)
                .furColor(requestDTO.getFurColor().toString())
                .sex(requestDTO.getSex())
                .build();
        features.forEach(feature -> reportAnimal.addReportedAnimalFeature(ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, feature)));


        List<Image> images = requestDTO.getImageUrls().stream()
                .map(url -> imageRepository.findByFilePath(String.valueOf(url))
                        .orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.IMAGE_NOT_FOUND))) //db에 일치하는 이미지가 없음
                .collect(Collectors.toList());


        Report report = Report.createReport(
                "실종신고",
                requestDTO.getLocation(),
                requestDTO.getMissingDate(),
                requestDTO.getDescription(),
                user,
                reportAnimal,
                images
        );

        reportRepository.save(report);
    }
}
