package com.kuit.findyou.global.common.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus{
    SUCCESS(20000, "요청에 성공했습니다."),
    BAD_REQUEST(40000, "유효하지 않은 요청입니다."),
    NOT_FOUND(40400, "존재하지 않는 API입니다."),


    // 관심글 등록 관련 에러
    USER_NOT_FOUND(40000, "존재하지 않는 유저입니다."),
    REPORT_NOT_FOUND(40000, "존재하지 않는 글입니다."),
    ALREADY_SAVED_INTEREST_REPORT(40000, "이미 관심글로 등록되었습니다."),


    INTERNAL_SERVER_ERROR(50000, "서버 내부 오류입니다.");

    private final boolean success = false;
    private final int code;
    private final String message;

    @Override
    public boolean getSuccess() { return this.success; }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
