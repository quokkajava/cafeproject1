package com.cafeproject.global.globalExceptionHandler;

import com.cafeproject.global.rsData.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 직접 던진 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RsData<Void>> handleBusiness(BusinessException e) {
        return toResponse(e.getError());
    }

    // @Valid 검증 실패 (MethodArgumentNotValidException 포함)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<RsData<List<FieldErrorDetail>>> handleValidation(BindException e) {
        List<FieldErrorDetail> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorDetail(fe.getField(), fe.getDefaultMessage()))
                .toList();

        com.cafeproject.global.globalExceptionHandler.ErrorType errorCode = CommonError.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(RsData.fail(errorCode, errors));
    }

    // 필수 쿼리 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RsData<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        return toResponse(CommonError.MISSING_PARAMETER);
    }

    // @RepuestParam, @PathVariable 검증 실패 (예: @Email, @NotBlank)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<RsData<Void>>
    handleMethodValidation(HandlerMethodValidationException e) {
        return toResponse(CommonError.INVALID_INPUT_VALUE);
    }

    // 타입 불일치 (예: /api/orders/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RsData<Void>>
    handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return toResponse(CommonError.INVALID_TYPE_VALUE);
    }

    // 지원하지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RsData<Void>> handleMethod(HttpRequestMethodNotSupportedException e) {
        return toResponse(CommonError.METHOD_NOT_ALLOWED);
    }

    // 존재하지 않는 URL (Spring Boot 3.2+)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RsData<Void>> handleNoResource(NoResourceFoundException e) {
        return toResponse(CommonError.NOT_FOUND);
    }

    // 그 외 예상하지 못한 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RsData<Void>> handleUnknown(Exception e) {
        return toResponse(CommonError.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<RsData<Void>> toResponse(ErrorType error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(RsData.fail(error));
    }

    public record FieldErrorDetail(String field, String reason) {}
}