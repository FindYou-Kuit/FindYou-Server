package com.kuit.findyou.domain.user.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class AlreadySavedInterestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public AlreadySavedInterestException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
