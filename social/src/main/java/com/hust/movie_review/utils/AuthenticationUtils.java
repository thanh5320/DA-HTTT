package com.hust.movie_review.utils;

import com.hust.movie_review.models.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.math.NumberUtils.createLong;

public class AuthenticationUtils {
    public static List<String> getRoles(Authentication authentication ) {
        if (authentication == null) return new ArrayList<>();
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static boolean isAdmin(Authentication authentication) {
        Set<String> asList = Stream.of("ADMIN", "SUPPER_ADMIN").collect(toSet());
        return getRoles(authentication).stream().anyMatch(asList::contains);
    }

    public static Integer loggedUserId(Authentication authentication) {
        if (authentication == null) return null;
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return Integer.valueOf(principal.getUser().getId());
    }

    public static boolean showLogInfo() {
        return false;
    }
}
