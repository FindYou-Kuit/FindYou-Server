package com.kuit.findyou.domain.auth.exception_handler;

import com.kuit.findyou.domain.auth.exception.AlreadySignedUpUserException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.ALREADY_SIGNED_UP_USER;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {

    // 유저가 존재하지 않는 경우
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadySignedUpUserException.class)
    public BaseErrorResponse handle_AlreadySignedUpUserException(Exception e) {
        log.error("[handle_AlreadySignedUpUserException]", e);
        return new BaseErrorResponse(ALREADY_SIGNED_UP_USER);
    }
}
