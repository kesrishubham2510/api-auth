package com.myreflectionthoughts.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreflectionthoughts.auth.datamodel.entity.User;
import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import com.myreflectionthoughts.auth.datamodel.request.LoginModel;
import com.myreflectionthoughts.auth.utility.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;
    private final ObjectMapper objectMapper;

//    @Value("${https.enabled}")
    private boolean  httpsEnabled;

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
                 --> Generate a HTTP only cookie and set the refresh token in that
            */

            LoginModel loginModel  =  objectMapper.readValue(request.getInputStream(), LoginModel.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword());

            Authentication auth = authenticationManager.authenticate(authenticationToken);

            if(auth.isAuthenticated()){

                UserAuth userAuth = (UserAuth) auth.getPrincipal();
                String token = "Bearer " + jwtHandler.generateJwtToken(userAuth.getUser());
                response.setHeader("Authorization", token);
                response.setStatus(HttpStatus.CREATED.value());

                generateAndSetRefreshToken(response, userAuth.getUser());

                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void generateAndSetRefreshToken(HttpServletResponse response, User user){

        String refreshToken = jwtHandler.generateRefreshToken(user);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api-auth/refresh-token");
        refreshCookie.setMaxAge(24*7*60*60); // 7 days
        refreshCookie.setSecure(httpsEnabled);
        response.addCookie(refreshCookie);

    }
}
