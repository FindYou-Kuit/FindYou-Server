package com.kuit.findyou.global.jwt.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;

public class InvalidJwtException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public InvalidJwtException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
