package com.hust.movie_review.data.request.user;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateUserRequest {
    @NotEmpty(message = "username is not empty!")
    private String username;

    @NotEmpty(message = "password is not empty!")
    private String password;

    @Email(message = "Email not validate")
    private String email;

    @NotEmpty(message = "role is not empty!")
    private Set<String> roles;
}
