package com.hust.movie_review.data.response.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class UserInfoResponse {
    private long id;
    private String username;
    private String fulName;
    private String email;
    @JsonIgnore
    private String password;
    private Set<String> roles;
    private boolean status;
}
