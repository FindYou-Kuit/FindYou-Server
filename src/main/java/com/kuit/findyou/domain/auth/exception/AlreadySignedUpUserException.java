package com.kuit.findyou.domain.auth.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class AlreadySignedUpUserException extends RuntimeException{

    private final ResponseStatus exceptionStatus;

    public AlreadySignedUpUserException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
