package com.kuit.findyou.global.common.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus{
    SUCCESS(20000, "요청에 성공했습니다."),
    BAD_REQUEST(40000, "유효하지 않은 요청입니다."),
    NOT_FOUND(40400, "존재하지 않는 API입니다."),


    //이미지 업로드 관련 에러
    NO_FILE_UPLOADED(40001, "업로드된 파일이 없습니다."),
    UPLOAD_ERROR(50001, "파일 업로드 중 에러가 발생했습니다."),
    IMAGE_NOT_FOUND(40400, "해당 URL의 이미지를 찾을 수 없습니다"),

    //게시글 등록 관련 에러
    BREED_NOT_FOUND(40000,"존재하지 않는 품종입니다"),


    // 관심글 등록 관련 에러
    USER_NOT_FOUND(40000, "존재하지 않는 유저입니다."),
    REPORT_NOT_FOUND(40000, "존재하지 않는 글입니다."),
    ALREADY_SAVED_INTEREST_REPORT(40000, "이미 관심글로 등록되었습니다."),

    INTEREST_ANIMAL_NOT_FOUND(40400, "존재하지 않는 관심동물입니다."),
    UNATHORIZED_USER(40100,  "권한이 없는 사용자의 요청입니다."),

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
