package com.kuit.findyou.global.common.exception_handler;

import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.exception.ReportNotFoundException;
import com.kuit.findyou.global.common.exception.UnauthorizedUserException;
import com.kuit.findyou.global.common.exception.UserNotFoundException;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    // 잘못된 요청일 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, TypeMismatchException.class})
    public BaseErrorResponse handle_BadRequest(Exception e){
        log.error("[handle_BadRequest]", e);
        return new BaseErrorResponse(BAD_REQUEST);
    }

    // 요청한 api가 없을 경우
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseErrorResponse handle_NoHandlerFoundException(Exception e){
        log.error("[handle_NoHandlerFoundException]", e);
        return new BaseErrorResponse(NOT_FOUND);
    }

    // 런타임 오류가 발생한 경우
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseErrorResponse handle_RuntimeException(Exception e) {
        log.error("[handle_RuntimeException]", e);
        return new BaseErrorResponse(INTERNAL_SERVER_ERROR);
    }

    // 유저가 존재하지 않는 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public BaseErrorResponse handle_UserNotFoundException(Exception e) {
        log.error("[handle_UsertNotFoundException]", e);
        return new BaseErrorResponse(USER_NOT_FOUND);
    }

    // 글이 존재하지 않는 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReportNotFoundException.class)
    public BaseErrorResponse handle_ReportNotFoundException(Exception e) {
        log.error("[handle_ReportNotFoundException]", e);
        return new BaseErrorResponse(REPORT_NOT_FOUND);
    }

    // 유저에게 권한이 존재하지 않는 경우
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedUserException.class)
    public BaseErrorResponse handle_UnauthorizedUserException(Exception e) {
        log.error("[handle_UnauthorizedUserException]", e);
        return new BaseErrorResponse(UNATHORIZED_USER);
    }
}
