package com.kuit.findyou.domain.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInterestAnimalRequest {
    private Long id;
    private String tag;
}
