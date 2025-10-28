package com.secure.security.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.apache.http.HttpStatus;

/**
 * @author sanwenyukaochi
 * @version 1.0
 * @since 2025-09-28
 */
@Builder
@JsonInclude
public record Result<T>(Integer code, String msg, T data) {

    public static <T> Result<T> success() {
        return Result.<T>builder().code(HttpStatus.SC_OK).msg("success").build();
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder().code(HttpStatus.SC_OK).msg("success").data(data).build();
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return Result.<T>builder().code(code).msg(msg).build();
    }

    public static <T> Result<T> error(Integer code, String msg, T data) {
        return Result.<T>builder().code(code).msg(msg).data(data).build();
    }

}
