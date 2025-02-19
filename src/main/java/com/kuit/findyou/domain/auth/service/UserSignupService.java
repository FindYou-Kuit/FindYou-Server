package com.kuit.findyou.domain.auth.service;

import com.kuit.findyou.domain.auth.dto.CheckDuplicateEmailRequest;
import com.kuit.findyou.domain.auth.dto.SignupRequest;
import com.kuit.findyou.domain.auth.exception.SameUserEmailExistsException;
import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.user.repository.UserRepository;
import com.kuit.findyou.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.SAME_USER_EMAIL_EXISTS;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public Long signup(SignupRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();
        log.info("[signup] email = {} password = {} j", email, password);
        if(alreadyExistentUser(email)){
            throw new SameUserEmailExistsException(SAME_USER_EMAIL_EXISTS);
        }
        User newUser = User.builder()
                .name(nickname)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    private boolean alreadyExistentUser(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkDuplicateEmail(String email) {
        if(alreadyExistentUser(email)){
            return true;
        }
        return false;
    }
}
