package com.secure.security.domain.model.dto;

import com.secure.security.common.web.constant.ResponseCodeConstants;

/**
 * 统一 JSON 返回结构
 *
 * @author sanwenyukaochi
 * @version 1.0
 * @since 2025-07-13
 */
public record Result<T>(
        String code,
        String message,
        T data
) {

    public static <T> Result<T> success() {
        return new Result<>(ResponseCodeConstants.SUCCESS, ResponseCodeConstants.SUCCESS, null);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(ResponseCodeConstants.SUCCESS, message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResponseCodeConstants.SUCCESS, message, data);
    }

    public static <T> Result<T> success(String code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(ResponseCodeConstants.ERROR, message, null);
    }

    public static <T> Result<T> error(String message, T data) {
        return new Result<>(ResponseCodeConstants.ERROR, message, data);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(ResponseCodeConstants.ERROR, ResponseCodeConstants.ERROR, data);
    }
}
