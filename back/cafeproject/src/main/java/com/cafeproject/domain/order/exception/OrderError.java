package com.cafeproject.domain.order.exception;

import com.cafeproject.global.globalExceptionHandler.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderError implements ErrorType {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "101", "주문을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
