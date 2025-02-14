package com.kuit.findyou.domain.auth.service;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    public void signup(String email, String password) {
        if(alreadyExistentUser(email)){
            throw new IllegalArgumentException("존재하는 사용자입니다");
        }
        User newUser = User.builder()
                .name("")
                .email(email)
                .password(password)
                .role("ROLE_USER")
                .build();
        userRepository.save(newUser);
    }

    private boolean alreadyExistentUser(String email) {
        return userRepository.existsByEmail(email);
    }
}
