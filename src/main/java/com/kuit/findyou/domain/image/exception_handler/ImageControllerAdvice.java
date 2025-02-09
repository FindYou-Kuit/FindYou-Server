package com.kuit.findyou.domain.image.exception_handler;

import com.kuit.findyou.domain.image.exception.FileStorageException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.INTERNAL_SERVER_ERROR;
@Slf4j
@RestControllerAdvice(basePackages = "com.kuit.findyou.domain.image")
public class ImageControllerAdvice {
    // 파일 업로드 실패
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileStorageException.class)
    public BaseErrorResponse handle_FileStorageException(FileStorageException e) {
        log.error("[handle_FileStorageException]", e);
        return new BaseErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
