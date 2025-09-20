package com.myreflectionthoughts.auth.jwt;

import com.myreflectionthoughts.auth.config.RestConstant;
import com.myreflectionthoughts.auth.utility.AppUtil;
import com.myreflectionthoughts.auth.utility.JwtHandler;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

        // need to bypass authorization header validation for login, signup and refresh
        if(StringUtils.isEmpty(tokenHeader)){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            String body  = AppUtil.loadMessageBody(RestConstant.MESSAGE_TEMPLATE);
            body  = body.replace("${message}", "Authorization header is required");
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.setContentType("application/json");
            response.getWriter().write(body);
            return;
        }else{
            String token = jwtHandler.extractToken(tokenHeader);
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);

            try{
                Authentication authResult = authenticationManager.authenticate(jwtAuthenticationToken);

                if (authResult.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                }

            }catch (JwtException jwtException){

                String content = AppUtil.loadMessageBody(RestConstant.FILENAME_JWT_TOKEN_ERROR);
                content = content.replace("${message}", AppUtil.handleMessage(jwtException.getMessage()));

                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.setContentType("application/json");
                response.getWriter().write(content);
                return;
            } catch (InternalAuthenticationServiceException internalAuthenticationServiceException){
                String content = AppUtil.loadMessageBody(RestConstant.MESSAGE_TEMPLATE);
                content = content.replace("${message}", "User does not exist, please check credentials");

                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                response.setContentType("application/json");
                response.getWriter().write(content);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    /*
      skip this filter and go to next one for the below servlet paths
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api-auth/login")
                || path.startsWith("/api-auth/register")
                || path.startsWith("/api-auth/refresh-token")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }

}
