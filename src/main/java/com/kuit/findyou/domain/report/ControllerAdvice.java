package com.kuit.findyou.domain.report;

import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    // 파일 업로드 실패
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileUploadException.class)
    public BaseErrorResponse handle_FileUploadException(FileUploadException e) {
        log.error("[handle_FileUploadException]", e);
        return new BaseErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
