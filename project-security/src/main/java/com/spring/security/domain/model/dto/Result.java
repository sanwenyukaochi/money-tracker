package com.spring.security.domain.model.dto;

import com.spring.security.common.web.constant.ResponseCodeConstants;

public record Result<T>(
        String code,
        String message,
        T data
) {

    @Deprecated(forRemoval = true)
    public static <T> Result<T> success() {
        return new Result<>(ResponseCodeConstants.SUCCESS, ResponseCodeConstants.SUCCESS, null);
    }

    @Deprecated(forRemoval = true)
    public static <T> Result<T> success(String message) {
        return new Result<>(ResponseCodeConstants.SUCCESS, message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResponseCodeConstants.SUCCESS, message, data);
    }

    @Deprecated(forRemoval = true)
    public static <T> Result<T> success(String code, String message, T data) {
        return new Result<>(code, message, data);
    }

    @Deprecated(forRemoval = true)
    public static <T> Result<T> error(String message) {
        return new Result<>(ResponseCodeConstants.ERROR, message, null);
    }

    @Deprecated(forRemoval = true)
    public static <T> Result<T> error(String message, T data) {
        return new Result<>(ResponseCodeConstants.ERROR, message, data);
    }

    public static <T> Result<T> error(String code, String message, T data) {
        return new Result<>(code, message, data);
    }

    @Deprecated(forRemoval = true)
    public static <T> Result<T> error(T data) {
        return new Result<>(ResponseCodeConstants.ERROR, ResponseCodeConstants.ERROR, data);
    }
}