package com.kuit.findyou.domain.user.repository;

import com.kuit.findyou.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByName(String name);

    User findByKakaoId(Long kakaoId);
}
