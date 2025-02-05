package com.kuit.findyou.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kuit.findyou.global.common.response.status.ResponseStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"success", "code", "message", "data"})
public class BaseErrorResponse implements ResponseStatus {
    private final boolean success;
    private final int code;
    private final String message;
    private final Object data;

    public BaseErrorResponse(ResponseStatus status) {
        this.success = false;
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = null;
    }

    public BaseErrorResponse(ResponseStatus status, String message) {
        this.success = false;
        this.code = status.getCode();
        this.message = message;
        this.data = null;
    }

    @Override
    public boolean getSuccess() {
        return this.success;
    }
    @Override
    public int getCode() {
        return this.code;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
