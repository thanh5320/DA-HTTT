package com.hust.movie_review.service;

import com.hust.movie_review.config.exception.InValidException;
import com.hust.movie_review.config.security.JwtUtils;
import com.hust.movie_review.data.request.user.CreateUserRequest;
import com.hust.movie_review.data.response.user.LoginResponse;
import com.hust.movie_review.data.response.user.UserInfoResponse;
import com.hust.movie_review.data.request.LoginRequest;
import com.hust.movie_review.models.Role;
import com.hust.movie_review.models.User;
import com.hust.movie_review.models.UserDetailsImpl;
import com.hust.movie_review.repositories.RoleRepository;
import com.hust.movie_review.repositories.UserRepository;
import com.hust.movie_review.service.template.IUserService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @Override
    public UserInfoResponse createUser(CreateUserRequest request) {
        checkAlreadyUser(request);

        // set property for new user
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());

        // get role object from role name string and assign for new user
        Set<Role> roles = Optional.ofNullable(request.getRoles())
                .orElse(new HashSet<>())
                .stream()
                .map(roleRepository::findByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        newUser.setRoles(roles);

        // save new user
        User userEntity = userRepository.save(newUser);

        // set response
        UserInfoResponse response = new UserInfoResponse();
        response.setEmail(userEntity.getEmail());
        response.setUsername(userEntity.getUsername());
        response.setFulName(userEntity.getFullName());
        response.setStatus(true);
        response.setRoles(request.getRoles());

        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request, AuthenticationManager authenticationManager) {
        // validate username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // set token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        // get role of user
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority().split("_")[1])
                .collect(Collectors.toSet());

        return new LoginResponse()
                .setId(userDetails.getUser().getId())
                .setUsername(userDetails.getUsername())
                .setToken(jwt)
                .setRoles(roles);
    }

    @SneakyThrows
    private void checkAlreadyUser(CreateUserRequest request) {
        User oldUSer;
        oldUSer = userRepository.findByUsername(request.getUsername());
        if (ObjectUtils.isNotEmpty(oldUSer)) {
            throw new InValidException("Username already exists");
        }
        oldUSer = userRepository.findByEmail(request.getEmail());
        if (request.getEmail()!=null && ObjectUtils.isNotEmpty(oldUSer)) {
            throw new InValidException("Email already exists");
        }
    }

    @SneakyThrows
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return UserDetailsImpl.build(user);
    }


}