package com.ohgiraffers.comprehensive.common.exception;

import com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int code;
    private String message;

    public ExceptionResponse(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
