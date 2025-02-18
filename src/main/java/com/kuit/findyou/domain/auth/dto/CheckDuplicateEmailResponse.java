package com.kuit.findyou.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckDuplicateEmailResponse {
    private Boolean isDuplicateEmail;
}
