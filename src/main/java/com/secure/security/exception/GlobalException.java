package com.secure.security.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    private final Integer code;

    public GlobalException(String message) {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        super(message);
    }

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
