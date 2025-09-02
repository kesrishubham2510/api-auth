package com.myreflectionthoughts.auth.jwt;

import com.myreflectionthoughts.auth.utility.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtValidator extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;
    private final ProviderManager authenticationManager;

    public JwtValidator(JwtHandler jwtHandler, ProviderManager authenticationManager){
        this.jwtHandler = jwtHandler;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader!=null){
            String token = jwtHandler.extractToken(tokenHeader);
            JwtAuthenticationToken jwtAuthenticatinToken = new JwtAuthenticationToken(token);

            Authentication authResult = authenticationManager.authenticate(jwtAuthenticatinToken);

            if(authResult.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
        }

        filterChain.doFilter(request, response);
    }
}
