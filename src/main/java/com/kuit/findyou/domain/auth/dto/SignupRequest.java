package com.kuit.findyou.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SignupRequest {
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+{}:;<>,.?/~`\\-=\\[\\]\\\\|]).{8,20}$", message = "비밀번호는 8-20자이어야 하고, 영문자,숫자,특수문자를 적어도 하나씩은 포함해야 합니다.")
    private String password;

    @NotBlank
    @Length(max = 8, message = "비밀번호는 최대 8자입니다.")
    private String nickname;
}
