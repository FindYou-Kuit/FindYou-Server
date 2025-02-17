package com.kuit.findyou.domain.auth.exception_handler;

import com.kuit.findyou.domain.auth.exception.SameUserEmailExistsException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {
    // 같은 이메일로 가입한 유저가 존재하는 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SameUserEmailExistsException.class)
    public BaseErrorResponse handle_SameUserEmailExistsException(Exception e) {
        log.error("[handle_SameUserEmailExistsException]", e);
        return new BaseErrorResponse(SAME_USER_EMAIL_EXISTS);
    }
}
