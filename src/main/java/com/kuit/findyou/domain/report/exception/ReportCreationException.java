package com.kuit.findyou.domain.report.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;

public class ReportCreationException extends RuntimeException {
    private final ResponseStatus status;

    public ReportCreationException(ResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public ReportCreationException(String message, Throwable cause, ResponseStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }
}