package com.ledger.ai.exception;

import com.ledger.ai.enums.BaseCode;
import com.ledger.ai.model.dto.Result;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private HttpStatus httpStatus;

    private String code;

    private String message;

    public BaseException() {
    }

    public BaseException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BaseException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BaseException(BaseCode baseCode) {
        super(baseCode.getMessage());
        this.code = baseCode.getCode();
        this.message = baseCode.getMessage();
        this.httpStatus = baseCode.getHttpStatus();
    }

    public BaseException(Result result, HttpStatus httpStatus) {
        super(result.message());
        this.code = result.code();
        this.message = result.message();
        this.httpStatus = httpStatus;
    }

    public BaseException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BaseException(String code, String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}