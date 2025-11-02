package com.secure.security.common.web.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.secure.security.domain.model.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class WebGlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("服务器异常", e);
        return Result.error("服务器异常");
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, NoResourceFoundException e) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return Result.builder()
                .message("api not found")
                .code(ResponseCodeConstants.API_NOT_FOUND)
                .build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, MethodArgumentNotValidException e) throws JsonProcessingException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        // 国际化翻译 数据校验异常信息
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        HashMap<String, String> errorFields = new HashMap<>();
        for (FieldError error : fieldErrors) {
            String fieldName = error.getField();
            errorFields.put(fieldName, error.getDefaultMessage());
        }
        return Result.error(objectMapper.writeValueAsString(errorFields));
    }

    @ExceptionHandler(value = BaseException.class)
    public Result<?> exceptionHandler(HttpServletResponse response, BaseException e) {
        response.setStatus(e.getHttpStatus().value());
        return createResult(e);
    }

    private Result<?> createResult(BaseException e) {
        return Result.builder()
                .message(e.getMessage())
                .code(e.getCode() == null ? ResponseCodeConstants.ERROR : e.getCode())
                .build();
    }
}
