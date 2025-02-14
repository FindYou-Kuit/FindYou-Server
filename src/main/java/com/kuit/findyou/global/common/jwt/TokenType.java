package com.kuit.findyou.global.common.jwt;

import lombok.Getter;

@Getter
public enum TokenType {

    ACCESS("access"),
    REFRESH("refresh");

    private final String type;

    TokenType(String type) {
        this.type = type;
    }


}
