package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.MissingReportDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.AnimalFeatureRepository;
import com.kuit.findyou.domain.report.repository.BreedRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void createReport(MissingReportDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Breed breed = breedRepository.findById(requestDTO.getBreedId()).orElseThrow(() -> new RuntimeException("품종을 찾을 수 없습니다."));
        List<AnimalFeature> features = animalFeatureRepository.findAllById(requestDTO.getFeatureIds());


        // ReportAnimal 객체 생성
        ReportAnimal reportAnimal = ReportAnimal.builder()
                .breed(breed)
                .furColor(requestDTO.getFurColor().toString())
                .sex(requestDTO.getSex())
                .build();
        features.forEach(feature -> reportAnimal.addReportedAnimalFeature(ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, feature)));

        // 이미지 URL을 사용하여 Image 객체 리스트 생성
        List<Image> images = requestDTO.getImageUrls().stream()
                .map(url -> Image.createImage(String.valueOf(url), UUID.randomUUID().toString()))
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
