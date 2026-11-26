package com.cafeproject.global.globalExceptionHandler;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorType error;

    public BusinessException(ErrorType error) {
        super(error.getMessage());
        this.error = error;
    }
}