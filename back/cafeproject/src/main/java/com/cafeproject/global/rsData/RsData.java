package com.cafeproject.global.rsData;

import com.cafeproject.global.globalExceptionHandler.ErrorType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RsData<T> {
    private final int statusCode;
    private final String code;
    private final String message;
    private final T data;

    private RsData(int statusCode, String code, String message, T data) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RsData(HttpStatus httpStatus, String message, T data) {
        this(httpStatus.value(), null, message, data);
    }

    public static RsData<Void> fail(ErrorType error) {
        return fail(error, null);
    }

    public static <T> RsData<T> fail(ErrorType error, T data) {
        return new RsData<>(
                error.getStatus().value(),
                error.getCode(),
                error.getMessage(),
                data
        );
    }
}