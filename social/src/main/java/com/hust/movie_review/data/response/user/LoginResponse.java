package com.hust.movie_review.data.response.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private long id;
    private String username;
    private String token;
    private Set<String> roles;
}
