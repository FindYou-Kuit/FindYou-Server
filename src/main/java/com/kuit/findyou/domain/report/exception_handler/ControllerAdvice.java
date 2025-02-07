package com.kuit.findyou.domain.report.exception_handler;

import com.kuit.findyou.domain.image.exception.FileStorageException;
import com.kuit.findyou.domain.report.exception.ReportCreationException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReportCreationException.class)
    public BaseErrorResponse handle_ReportCreationException(ReportCreationException e) {
        log.error("[handle_ReportCreationException]", e);
        return new BaseErrorResponse(BAD_REQUEST, e.getMessage());
    }
}
