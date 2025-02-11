package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.breed.model.Breed;
import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.image.model.Image;
import com.kuit.findyou.domain.report.dto.request.MissingReportDTO;
import com.kuit.findyou.domain.report.exception.ReportCreationException;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.AnimalFeatureRepository;
import com.kuit.findyou.domain.breed.repository.BreedRepository;
import com.kuit.findyou.domain.image.repository.ImageRepository;
import com.kuit.findyou.domain.report.repository.ReportRepository;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Breed breed = breedRepository.findById(requestDTO.getBreed()).orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.BREED_NOT_FOUND));
        List<AnimalFeature> features = animalFeatureRepository.findAllById(requestDTO.getFeatures());


        // ReportAnimal 객체 생성
        ReportAnimal reportAnimal = ReportAnimal.builder()
                .breed(breed)
                .furColor(String.join(", ", requestDTO.getFurColor()))
                .sex(requestDTO.getSex())
                .build();
        features.forEach(feature -> ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, feature));


        List<Image> images = requestDTO.getImageUrls().stream()
                .map(url -> imageRepository.findByFilePath(String.valueOf(url))
                        .orElseThrow(() -> new ReportCreationException(BaseExceptionResponseStatus.IMAGE_NOT_FOUND))) //db에 일치하는 이미지가 없음
                .collect(Collectors.toList());


        Report report = Report.createReport(
                ReportTag.MISSING,
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
