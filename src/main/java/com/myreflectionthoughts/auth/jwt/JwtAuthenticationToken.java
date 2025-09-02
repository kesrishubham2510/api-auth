package com.myreflectionthoughts.auth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String token;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    public String getToken() {
        return token;
    }
}
