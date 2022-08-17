package com.hust.movie_review.config.exception;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class ApiException extends RuntimeException {
    static final long serialVersionUID = 1651914954615L;
    private final int code;
    private Map<String, Object> data;

    public ApiException(String message) {
        super(message);
        this.code = BAD_REQUEST.value();
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.code = BAD_REQUEST.value();
    }

    public ApiException(int statusCode, String s) {
        super(s);
        this.code = statusCode;
    }

    public ApiException(int statusCode, Map<String, Object> data) {
        this.code = statusCode;
        this.data = data;
    }

    public ApiException(String filed, String message) {
        this.code = 422;
        Map<String, Object> data = new HashMap<>();
        data.put(filed, message);
        this.data = data;
    }

    public ApiException(int statusCode, String s, Throwable cause) {
        super(s, cause);
        this.code = statusCode;
    }
}