package com.kuit.findyou.domain.auth.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignUpRequestDTO {

    private String name;

    private String email;

    private String password;

    private Long kakaoId;

    private String profileImageUrl;
}
