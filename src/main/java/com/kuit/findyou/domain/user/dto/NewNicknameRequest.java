package com.kuit.findyou.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewNicknameRequest {

    @Schema(description = "새롭게 변경할 닉네임입니다.")
    private String newNickname;
}
