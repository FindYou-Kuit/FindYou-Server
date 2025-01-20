package com.kuit.findyou.domain.report.exception;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException{
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
