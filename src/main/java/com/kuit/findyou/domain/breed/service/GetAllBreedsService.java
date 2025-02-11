package com.kuit.findyou.domain.breed.service;

import com.kuit.findyou.domain.breed.dto.response.BreedResponseDTO;
import com.kuit.findyou.domain.breed.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllBreedsService {

    private final BreedRepository breedRepository;

    @Cacheable(cacheNames = "getBreeds", key = "'breeds:all'", cacheManager = "breedCacheManager")
    public List<BreedResponseDTO> getAllBreeds() {
        return breedRepository.findAll().stream()
                .map(breed -> BreedResponseDTO.builder()
                        .breedId(breed.getId())
                        .breedName(breed.getName())
                        .species(breed.getSpecies())
                        .build())
                .collect(Collectors.toList());
    }
}
