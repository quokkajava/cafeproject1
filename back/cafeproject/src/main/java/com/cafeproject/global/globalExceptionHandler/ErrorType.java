package com.cafeproject.global.globalExceptionHandler;

import org.springframework.http.HttpStatus;

public interface ErrorType {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}