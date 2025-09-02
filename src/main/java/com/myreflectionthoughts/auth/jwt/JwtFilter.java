package com.myreflectionthoughts.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import com.myreflectionthoughts.auth.datamodel.request.LoginModel;
import com.myreflectionthoughts.auth.utility.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;
    private final ObjectMapper objectMapper;

    public JwtFilter(ProviderManager providerManager, JwtHandler jwtHandler, ObjectMapper objectMapper){
        this.authenticationManager = providerManager;
        this.jwtHandler = jwtHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // check if the endpoint is /generate-token
        if(request.getRequestURI().equals("/api-auth/generate-token")){

            /*
                 --> Retrieve the request body
                 --> Authenticate the user, on successful authentication generate the token
         */

            LoginModel loginModel  =  objectMapper.readValue(request.getInputStream(), LoginModel.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword());

            Authentication auth = authenticationManager.authenticate(authenticationToken);

            if(auth.isAuthenticated()){
                UserAuth userAuth = (UserAuth) auth.getPrincipal();
                String token = "Bearer " + jwtHandler.generateToken(userAuth.getUser());
                response.setHeader("Authorization", token);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
