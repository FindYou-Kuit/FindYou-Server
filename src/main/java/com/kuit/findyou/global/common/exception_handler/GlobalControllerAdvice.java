package com.kuit.findyou.global.common.exception_handler;

import com.kuit.findyou.global.common.exception.*;
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import com.kuit.findyou.global.jwt.exception.InvalidJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.*;

@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    // 잘못된 요청일 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, TypeMismatchException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
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
        return new BaseErrorResponse(UNAUTHORIZED_USER);
    }

    // Multipart 요청에 실패한 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public BaseErrorResponse handle_MultipartException(MultipartException e) {
        log.error("[handle_MultipartException]", e);
        return new BaseErrorResponse(BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseErrorResponse handle_MaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        log.error("[handle_MaxUploadSizeExceeded]", e);
        return new BaseErrorResponse(UPLOAD_SIZE_EXCEEDED);
    }

    // 유효하지 않은 토큰인 경우
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({SignatureException.class, MalformedJwtException.class, ExpiredJwtException.class, InvalidJwtException.class})
    public BaseErrorResponse handle_InvalidJwtException(InvalidJwtException e) {
        log.error("[handle_InvalidJwtException]", e);
        return new BaseErrorResponse(INVALID_TOKEN);
    }

    // 토큰이 없는 경우
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtNotFoundException.class)
    public BaseErrorResponse handle_JwtNotFoundException(JwtNotFoundException e) {
        log.error("[handle_JwtNotFoundException]", e);
        return new BaseErrorResponse(TOKEN_NOT_FOUND);
    }
}
