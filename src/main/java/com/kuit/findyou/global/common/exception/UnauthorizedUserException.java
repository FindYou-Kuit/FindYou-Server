package com.kuit.findyou.global.common.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class UnauthorizedUserException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public UnauthorizedUserException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
