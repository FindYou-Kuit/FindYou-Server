package com.kuit.findyou.domain.user.exception_handler;

import com.kuit.findyou.domain.user.exception.AlreadySavedInterestException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestControllerAdvice
public class UserControllerAdvice {
    // 유저가 존재하지 않는 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadySavedInterestException.class)
    public BaseErrorResponse handle_AlreadySavedInterestException(Exception e) {
        log.error("[handle_AlreadySavedInterestException]", e);
        return new BaseErrorResponse(ALREADY_SAVED_INTEREST_REPORT);
    }
}
