package com.kuit.findyou.domain.breed.repository;

import com.kuit.findyou.domain.breed.model.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {

    @Query("SELECT b " +
            "FROM Breed b " +
            "WHERE REPLACE(TRIM(b.name), ' ', '') = REPLACE(TRIM(:breedName), ' ', '')")
    Optional<Breed> findByTrimmedBreedName(@Param("breedName") String breedName);

}
