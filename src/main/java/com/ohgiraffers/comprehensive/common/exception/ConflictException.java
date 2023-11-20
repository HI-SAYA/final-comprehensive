package com.ohgiraffers.comprehensive.common.exception;

import com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ConflictException extends CustomException {

    public ConflictException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
