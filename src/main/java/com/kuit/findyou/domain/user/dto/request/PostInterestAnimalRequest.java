package com.kuit.findyou.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "관심동물 추가 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInterestAnimalRequest {
    @Schema(description = "관심동물로 추가할 동물의 id")
    private Long id;
}
