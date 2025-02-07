package com.kuit.findyou.domain.image.exception;

import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public FileStorageException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public FileStorageException(String message, Throwable cause, ResponseStatus exceptionStatus) {
        super(message, cause);
        this.exceptionStatus = exceptionStatus;
    }
}
