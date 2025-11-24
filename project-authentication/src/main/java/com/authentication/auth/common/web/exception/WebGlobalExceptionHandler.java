package com.authentication.auth.common.web.exception;

import com.authentication.auth.common.web.constant.ResponseCodeConstants;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.authentication.auth.domain.model.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.warn("系统异常：code={}, msg={}", ResponseCodeConstants.SYSTEM_ERROR, e.getMessage());
        return Result.error(ResponseCodeConstants.SYSTEM_ERROR,"系统异常");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        log.warn("请求方法异常：code={}, msg={}", ResponseCodeConstants.HTTP_METHOD_NOT_ALLOWED, e.getMessage());
        return Result.error(ResponseCodeConstants.HTTP_METHOD_NOT_ALLOWED,"请求方法不支持"+ e.getMethod());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, NoResourceFoundException e) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        log.warn("资源找不到：code={}, msg={}", ResponseCodeConstants.RESOURCE_NOT_FOUND, e.getMessage());
        return Result.error(ResponseCodeConstants.RESOURCE_NOT_FOUND,e.getMessage());
    }

}
