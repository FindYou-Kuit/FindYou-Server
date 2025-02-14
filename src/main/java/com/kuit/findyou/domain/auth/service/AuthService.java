package com.kuit.findyou.domain.auth.service;

import com.kuit.findyou.domain.auth.dto.request.SignUpRequestDTO;
import com.kuit.findyou.domain.auth.exception.AlreadySignedUpUserException;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.user.model.User;
import com.kuit.findyou.global.common.exception.InvalidTokenException;
import com.kuit.findyou.global.common.jwt.JwtUtil;
import com.kuit.findyou.global.common.jwt.TokenType;
import com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kuit.findyou.global.common.jwt.Header.*;
import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisService redisService;

    @Transactional
    public void signUp(SignUpRequestDTO request) {
        String name = request.getName();
        String email = request.getEmail();
        Long kakaoId = request.getKakaoId();
        String profileImageUrl = request.getProfileImageUrl();

        Boolean isExist = userRepository.existsByName(name);

        if (isExist) {
            throw new AlreadySignedUpUserException(ALREADY_SIGNED_UP_USER);
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(bCryptPasswordEncoder.encode("password")) // password 를 임의의 값으로 채움 - 사용하지 않음
                .kakaoId(kakaoId)
                .profileImageUrl(profileImageUrl)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtUtil.extractTokenFromHeader(request, REFRESH.getKey());

        if (refreshToken == null) {
            throw new InvalidTokenException(REFRESH_TOKEN_NOT_FOUND);
        }

        jwtUtil.validateToken(refreshToken); // 여기서는 true 를 반환하거나 예외를 발생시킴

        String type = jwtUtil.extractTypeFromToken(refreshToken);

        if (!type.equals(TokenType.REFRESH.getType())) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }

        // DB에 존재하는 refresh 토큰인지 확인
        Boolean isExist = redisService.isRefreshTokenExist(refreshToken);
        if (!isExist) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }

        // DB에 존재한다면 요청으로 들어온 Refresh 토큰 값이 DB에 저장되어있는 Refresh 토큰 값과 동일한지 확인
        if (!redisService.getRefreshToken(refreshToken).equals(refreshToken)) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }

        // 새로운 access 토큰 발급
        Long userId = jwtUtil.extractUserIdFromToken(refreshToken);

        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // RefreshToken 저장소에서 기존의 RefreshToken 은 삭제 후 새 RefreshToken 저장
        redisService.deleteRefreshToken(refreshToken);
        redisService.saveRefreshToken(newRefreshToken);

        // 응답
        response.setHeader(AUTHORIZATION.getKey(), BEARER.getKey() + newAccessToken);
        response.setHeader(REFRESH.getKey(), BEARER.getKey() + newRefreshToken);
    }


}
