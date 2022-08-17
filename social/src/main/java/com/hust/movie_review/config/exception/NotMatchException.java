package com.hust.movie_review.config.exception;

import lombok.Data;

@Data
public class NotMatchException extends java.lang.Exception {
    public static final int CODE = 409;
    public static final String DEFAULT_MESSAGE = "No data matching";
    public NotMatchException(){super(DEFAULT_MESSAGE);}
    public NotMatchException(String message){
        super(message);
    }
}
