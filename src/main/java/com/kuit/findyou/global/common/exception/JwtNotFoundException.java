package com.kuit.findyou.global.common.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;

public class JwtNotFoundException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public JwtNotFoundException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
