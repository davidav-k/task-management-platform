package com.example.user_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = (UsernamePasswordAuthenticationToken) authentication;
        var userFromDB = userDetailsService.loadUserByUsername((String) user.getPrincipal());
        var password = (String) user.getCredentials();
        if (userFromDB.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(userFromDB, "[PASSWORD_PROTECTED]", userFromDB.getAuthorities());
        }
        throw new BadCredentialsException("Unable to login");
    }
}
