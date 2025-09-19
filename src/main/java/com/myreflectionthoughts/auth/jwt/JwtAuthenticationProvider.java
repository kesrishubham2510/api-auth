package com.myreflectionthoughts.auth.jwt;

import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import com.myreflectionthoughts.auth.dataprovider.service.AuthProvider;
import com.myreflectionthoughts.auth.utility.JwtHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtHandler jwtHandler;
    private final AuthProvider authProvider;

    public JwtAuthenticationProvider(JwtHandler jwtHandler, AuthProvider authProvider){
        this.authProvider = authProvider;
        this.jwtHandler = jwtHandler;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = ((JwtAuthenticationToken) authentication).getToken();

        String username = jwtHandler.extractUsername(token);

        if(username==null){
            throw new BadCredentialsException("The token is invalid");
        }

        UserAuth userauth = (UserAuth) authProvider.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userauth, "", userauth.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
