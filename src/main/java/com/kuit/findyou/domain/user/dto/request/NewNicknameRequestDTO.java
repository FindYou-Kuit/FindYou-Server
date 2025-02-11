package com.kuit.findyou.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewNicknameRequestDTO {

    @NotBlank
    @Schema(description = "새롭게 변경할 닉네임입니다. null, 빈 문자열, 공백이어서는 안됩니다.")
    private String newNickname;
}
