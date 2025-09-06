package com.myreflectionthoughts.auth.jwt;

import com.myreflectionthoughts.auth.config.RestConstant;
import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import com.myreflectionthoughts.auth.utility.AppUtil;
import com.myreflectionthoughts.auth.utility.JwtHandler;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;
    private final ProviderManager authenticationManager;

    public JwtRefreshFilter(JwtHandler jwtHandler, ProviderManager authenticationManager){
        this.jwtHandler = jwtHandler;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().equals("/api-auth/refresh-token")){
            /*
                --> when the token is expired, it won't come from FE in header
                --> Retrieve the refreshToken from cookie
                --> Validate the refreshToken
                --> Create a new JWT token and set it in Header
            */

            String refreshToken = jwtHandler.retrieveRefreshTokenCookie(request);

            if(refreshToken==null){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            }

            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(refreshToken);

            try {
                Authentication auth = authenticationManager.authenticate(jwtAuthenticationToken);

                if(auth.isAuthenticated()){
                    UserAuth userAuth = (UserAuth) auth.getPrincipal();
                    String newToken = "Bearer " + jwtHandler.generateRefreshToken(userAuth.getUser());
                    response.setHeader("Authorization", newToken);

                    String body  = AppUtil.loadMessageBody(RestConstant.MESSAGE_TEMPLATE);
                    body  = body.replace("${message}", "JWT token is generated & set in response, please check for `Authorization` header in response");

                    response.getWriter().write(body);
                    return;
                }

            }catch (JwtException jwtException){
                String content = AppUtil.loadMessageBody(RestConstant.FILENAME_JWT_TOKEN_ERROR);
                content = content.replace("${message}", AppUtil.handleMessage(jwtException.getMessage()));

                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(content);
                return;
            } catch (InternalAuthenticationServiceException internalAuthenticationServiceException){

                String content = AppUtil.loadMessageBody(RestConstant.MESSAGE_TEMPLATE);
                content = content.replace("${message}", "User does not exist, please check credentials");

                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(content);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}
