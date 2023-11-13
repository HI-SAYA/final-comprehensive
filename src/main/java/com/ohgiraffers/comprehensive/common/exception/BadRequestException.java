package com.ohgiraffers.comprehensive.common.exception;

import com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    // 코드랑 메세지를 담아서 응답할 용도

    private final int code;
    private final String message;


        public BadRequestException(final ExceptionCode exceptionCode) {
            this.code = exceptionCode.getCode();
            this.message = exceptionCode.getMessage();
    }
}
