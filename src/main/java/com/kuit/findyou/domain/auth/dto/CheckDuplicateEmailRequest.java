package com.kuit.findyou.domain.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class CheckDuplicateEmailRequest {
    @Email
    private String email;
}
