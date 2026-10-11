package com.servicehub.security;

import com.servicehub.exception.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UnauthorizedAccessException("You are not logged in");
        }
        return auth.getName();
    }

    public static boolean hasRole(Role role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        String expected = "ROLE_" + role.name();
        return auth.getAuthorities().stream()
                .anyMatch(a -> expected.equals(a.getAuthority()));
    }

    public static boolean isLoggedInCustomer(String email) {
        return hasRole(Role.CUSTOMER) && currentEmail().equals(email);
    }

    public static boolean isLoggedInProvider(String email) {
        return hasRole(Role.PROVIDER) && currentEmail().equals(email);
    }
}