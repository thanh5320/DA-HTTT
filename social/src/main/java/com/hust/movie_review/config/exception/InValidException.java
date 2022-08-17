package com.hust.movie_review.config.exception;

import lombok.Data;

@Data
public class InValidException extends java.lang.Exception {
    public static final int CODE = 422;
    public static final String DEFAULT_MESSAGE = "Your request is inValid";
    public InValidException(String message){
     super(message);
    }

    public InValidException(){
        super(DEFAULT_MESSAGE);
    }
}
