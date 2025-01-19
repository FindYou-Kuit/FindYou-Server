package com.kuit.findyou.domain.user.controller;

import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.service.UserService;
import com.kuit.findyou.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("interest-animals")
    public BaseResponse<Long> postInterestAnimal(@RequestBody PostInterestAnimalRequest request){
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        log.info("[postInterestAnimal] userId = {} request = {}", userId, request);
        Long id = userService.saveInterestAnimal(userId, request);
        log.info("[postInterestAnimal] id = {}", id);
        return new BaseResponse<>(null);
    }
}
