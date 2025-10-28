package com.secure.security.handler;

import com.secure.security.exception.GlobalException;
import com.secure.security.model.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Result<?>> handleException(GlobalException e) {
        log.warn("业务异常：code={}, msg={}", e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getCode()).body(
                Result.error(e.getCode(), e.getMessage())
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<?>> handleException(AuthenticationException e) {
        log.warn("未授权错误：code={}, msg={}", HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(
                Result.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<?>> handleException(AccessDeniedException e) {
        log.warn("权限不足：code={}, msg={}", HttpStatus.FORBIDDEN.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(
                Result.error(HttpStatus.FORBIDDEN.value(), e.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<?>> handleException(MethodArgumentNotValidException e) {
        StringBuilder strBuilder = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> strBuilder
                .append("[").append(fieldError.getField()).append("]")
                .append(fieldError.getDefaultMessage()).append(";")
        );
        log.warn("参数错误：code={}, msg={}", HttpStatus.BAD_REQUEST.value(), strBuilder);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                Result.error(HttpStatus.BAD_REQUEST.value(), strBuilder.toString())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleException(Exception e) {
        log.warn("系统异常：code={}, msg={}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常")
        );
    }

}
