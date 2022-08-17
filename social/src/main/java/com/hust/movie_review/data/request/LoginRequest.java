package com.hust.movie_review.data.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "username is not empty!")
    private String username;
    @NotEmpty(message = "password is not empty!")
    private String password;
}
