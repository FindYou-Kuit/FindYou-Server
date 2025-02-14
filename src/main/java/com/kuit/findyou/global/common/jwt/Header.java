package com.kuit.findyou.global.common.jwt;

import lombok.Getter;

@Getter
public enum Header {
    AUTHORIZATION("Authorization"),
    BEARER("Bearer "),
    REFRESH("Refresh");

    Header(String key) {
        this.key = key;
    }

    private final String key;
}
