package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.report.dto.BreedValidateResponseDTO;
import com.kuit.findyou.domain.report.model.Breed;
import com.kuit.findyou.domain.report.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreedValidateService {

    private final BreedRepository breedRepository;

    public BreedValidateResponseDTO validateBreed(String breedName) {
        Breed findBreed = breedRepository.findByTrimmedBreedName(breedName)
                .orElse(null);

        if(findBreed == null) {
            return BreedValidateResponseDTO.builder()
                    .breedId(-1L)
                    .isExist(false)
                    .build();
        }

        return BreedValidateResponseDTO.builder()
                .breedId(findBreed.getId())
                .isExist(true)
                .build();
    }
}
