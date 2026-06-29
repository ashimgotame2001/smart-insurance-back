package com.project.smartinsurance.commonService.exception;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource errorMessageSource;

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(GlobalException ex) {
        log.error("Business exception [{}]: {}", ex.getCode(), ex.getMessage(), ex);
        String message = errorMessageSource.getMessage(ex.getCode(), ex.getArgs(), ex.getCode(), LocaleContextHolder.getLocale());
        ApiResponse<Object> response = ApiResponse.error(ex.getCode(), message);
        HttpStatus status = resolveHttpStatus(ex.getCode());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch for parameter '{}' with value '{}'", ex.getName(), ex.getValue());
        String message = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());
        ApiResponse<Object> response = ApiResponse.error("SYS-002", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", details);
        ApiResponse<Object> response = ApiResponse.error("SYS-003", details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        log.warn("Missing required parameter: {}", ex.getParameterName());
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
        ApiResponse<Object> response = ApiResponse.error("SYS-004", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body", ex);
        ApiResponse<Object> response = ApiResponse.error("SYS-005", "Malformed request body");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.error("CHK-001", "Access denied");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not supported: {}", ex.getMessage());
        ApiResponse<Object> response = ApiResponse.error("SYS-006", "Request method not supported");
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        String code = "SYS-001";
        String message = errorMessageSource.getMessage(code, null, "Internal server error. Please contact system administrator.", LocaleContextHolder.getLocale());
        ApiResponse<Object> response = ApiResponse.error(code, message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus resolveHttpStatus(String code) {
        if (code == null) return HttpStatus.BAD_REQUEST;
        if (code.startsWith("AUTH-")) return HttpStatus.UNAUTHORIZED;
        if (code.startsWith("CHK-")) return HttpStatus.FORBIDDEN;
        if (code.startsWith("SYS-")) return HttpStatus.INTERNAL_SERVER_ERROR;
        return HttpStatus.BAD_REQUEST;
    }
}
