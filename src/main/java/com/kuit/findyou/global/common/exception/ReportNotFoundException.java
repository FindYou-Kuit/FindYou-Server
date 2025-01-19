package com.kuit.findyou.global.common.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class ReportNotFoundException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public ReportNotFoundException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
