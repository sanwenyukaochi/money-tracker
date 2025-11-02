package com.secure.security.domain.model.dto;

import com.secure.security.common.web.constant.ResponseCodeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 JSON 返回结构
 *
 * @author sanwenyukaochi
 * @version 1.0
 * @since 2025-07-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private String code;
    private String message;
    private T data;

    // -------------------- Success --------------------
    public static <T> Result<T> success() {
        return Result.<T>builder().code(ResponseCodeConstants.SUCCESS).message(ResponseCodeConstants.SUCCESS).data(null).build();
    }

    public static <T> Result<T> success(String message) {
        return Result.<T>builder().code(ResponseCodeConstants.SUCCESS).message(message).data(null).build();
    }

    public static <T> Result<T> success(String message, T data) {
        return Result.<T>builder().code(ResponseCodeConstants.SUCCESS).message(message).data(data).build();
    }

    public static <T> Result<T> error(String message) {
        return Result.<T>builder().code(ResponseCodeConstants.ERROR).message(message).data(null).build();
    }

    public static <T> Result<T> error(String message, T data) {
        return Result.<T>builder().code(ResponseCodeConstants.ERROR).message(message).data(data).build();
    }

    public static <T> Result<T> error(T data) {
        return Result.<T>builder().code(ResponseCodeConstants.ERROR).message(ResponseCodeConstants.ERROR).data(data).build();
    }
}
