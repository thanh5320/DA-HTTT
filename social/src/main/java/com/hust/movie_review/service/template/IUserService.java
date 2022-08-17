package com.hust.movie_review.service.template;

import com.hust.movie_review.data.request.user.CreateUserRequest;
import com.hust.movie_review.data.response.user.LoginResponse;
import com.hust.movie_review.data.response.user.UserInfoResponse;
import com.hust.movie_review.data.request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserInfoResponse createUser(CreateUserRequest request);
    LoginResponse login(LoginRequest request, AuthenticationManager authenticationManager);
}
