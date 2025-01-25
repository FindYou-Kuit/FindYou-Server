package com.kuit.findyou.domain.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewNicknameRequest {

    private String newNickname;
}
