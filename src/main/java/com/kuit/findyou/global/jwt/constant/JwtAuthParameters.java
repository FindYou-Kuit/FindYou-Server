package com.kuit.findyou.global.jwt.constant;

import lombok.Getter;

@Getter
public enum JwtAuthParameters {
    LOGIN_ENDPOINT("/api/v1/auth/login"),

    PARAMETER_MAPPED_TO_USERNAME("kakaoId"),

    DEFAULT_PASSWORD("password");

    private String value;

    JwtAuthParameters(String value) {
        this.value = value;
    }
}
