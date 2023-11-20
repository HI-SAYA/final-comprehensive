package com.ohgiraffers.comprehensive.common.exception;

import com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {
    // 코드랑 메세지를 담아서 응답할 용도

        public BadRequestException(final ExceptionCode exceptionCode) {
            super(exceptionCode);
    }
}
