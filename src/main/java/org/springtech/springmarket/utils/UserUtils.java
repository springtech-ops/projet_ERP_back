package org.springtech.springmarket.utils;

import org.springframework.security.core.Authentication;
import org.springtech.springmarket.domain.UserPrincipal;
import org.springtech.springmarket.dto.UserDTO;

public class UserUtils {
    public static UserDTO getAuthenticatedUser(Authentication authentication) {
        return ((UserDTO) authentication.getPrincipal());
    }

    public static UserDTO getLoggedInUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
}
