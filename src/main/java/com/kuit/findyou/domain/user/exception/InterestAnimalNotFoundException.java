package com.kuit.findyou.domain.user.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class InterestAnimalNotFoundException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public InterestAnimalNotFoundException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
