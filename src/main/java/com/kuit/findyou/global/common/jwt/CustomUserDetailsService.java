package com.kuit.findyou.global.common.jwt;

import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {

        // 얘를 카카오 ID로 바꾸면 됨.
        User user = userRepository.findByKakaoId(Long.parseLong(kakaoId));

        if (user != null) {
            return new CustomUserDetails(user);
        }

        return null;
    }
}
