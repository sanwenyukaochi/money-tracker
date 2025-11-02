package com.secure.security.common.web.exception;

import com.secure.security.common.web.constant.ResponseCodeConstants;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.secure.security.domain.model.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class WebGlobalExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, BaseException e) {
        response.setStatus(e.getHttpStatus().value());
        log.warn("业务异常：code={}, msg={}", e.getCode(), e.getMessage());
        return Result.builder().code(e.getCode()).message(e.getMessage()).build();
    }

    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.warn("系统异常：code={}, msg={}", ResponseCodeConstants.SYSTEM_ERROR, e.getMessage());
        return Result.builder().code(ResponseCodeConstants.SYSTEM_ERROR).message("系统异常").build();
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, NoResourceFoundException e) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        log.warn("资源找不到：code={}, msg={}", ResponseCodeConstants.RESOURCE_NOT_FOUND, e.getMessage());
        return Result.builder().code(ResponseCodeConstants.RESOURCE_NOT_FOUND).message(e.getMessage()).build();
    }

}
