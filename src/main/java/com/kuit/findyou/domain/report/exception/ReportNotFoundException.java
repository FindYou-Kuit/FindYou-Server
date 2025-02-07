package com.kuit.findyou.domain.report.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;

public class ReportNotFoundException extends RuntimeException{
    private final ResponseStatus status;

    public ReportNotFoundException(ResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }
}
