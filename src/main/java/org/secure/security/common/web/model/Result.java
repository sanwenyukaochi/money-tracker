package org.secure.security.common.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应信息主体
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    public static final String SUCCESS_CODE = "success";
    public static final String ERROR_CODE = "error";
    private String code;
    private String message;
    private Object data;

    public static Result success() {
        return Result.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_CODE)
                .data(null)
                .build();
    }

    public static Result success(String message) {
        return Result.builder()
                .code(SUCCESS_CODE)
                .message(message)
                .data(null)
                .build();
    }

    public static Result success(String message, Object data) {
        return Result.builder()
                .code(SUCCESS_CODE)
                .message(message)
                .data(data)
                .build();
    }

    public static Result error(Object data, String msg) {
        return Result.builder()
                .code(ERROR_CODE)
                .message(msg)
                .data(data)
                .build();
    }

    public static Result error(String msg) {
        return Result.builder()
                .code(ERROR_CODE)
                .message(msg)
                .data(null)
                .build();
    }

    public static Result error(Object data) {
        return Result.builder()
                .code(ERROR_CODE)
                .message(ERROR_CODE)
                .data(data)
                .build();
    }
}
