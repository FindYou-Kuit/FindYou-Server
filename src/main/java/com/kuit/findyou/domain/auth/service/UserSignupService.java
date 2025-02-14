package com.kuit.findyou.domain.auth.service;

import com.kuit.findyou.domain.auth.exception.SameUserEmailExistsException;
import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.SAME_USER_EMAIL_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    public void signup(String email, String password) {
        log.info("[signup] email = {} password = {}", email, password);
        if(alreadyExistentUser(email)){
            throw new SameUserEmailExistsException(SAME_USER_EMAIL_EXISTS);
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
