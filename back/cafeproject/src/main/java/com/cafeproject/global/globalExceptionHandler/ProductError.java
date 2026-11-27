package com.cafeproject.global.globalExceptionHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductError implements ErrorType {
    NOT_FOUND(HttpStatus.NOT_FOUND, "101", "요청한 상품을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}