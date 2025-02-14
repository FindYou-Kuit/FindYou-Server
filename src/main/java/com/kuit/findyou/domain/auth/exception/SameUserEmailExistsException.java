package com.kuit.findyou.domain.auth.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;

public class SameUserEmailExistsException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public SameUserEmailExistsException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}