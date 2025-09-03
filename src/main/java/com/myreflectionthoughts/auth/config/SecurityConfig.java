package com.myreflectionthoughts.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreflectionthoughts.auth.dataprovider.service.AuthProvider;
import com.myreflectionthoughts.auth.jwt.JwtAuthenticationProvider;
import com.myreflectionthoughts.auth.jwt.JwtFilter;
import com.myreflectionthoughts.auth.jwt.JwtRefreshFilter;
import com.myreflectionthoughts.auth.jwt.JwtValidator;
import com.myreflectionthoughts.auth.utility.JwtHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthProvider authProvider;
    private final JwtHandler jwtHandler;
    private final ObjectMapper objectMapper;

    public SecurityConfig( BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuthProvider authProvider,
                           JwtHandler jwtHandler,
                           ObjectMapper objectMapper){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authProvider = authProvider;
        this.jwtHandler = jwtHandler;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        /*
            --> When the swagger ui is accessed, the server creates a JSESSIONID and then expects csrf token as well
                in all further requests to validate the JESSIONID

            --> Since this service is JWT/stateless, CSRF protection is unnecessary and will block requests
                from clients like Postman or Swagger (because they won’t automatically include a CSRF token).
                Hence, CSRF is disabled.

            --> By default, all endpoints are authenticated. Hence, signup and swagger pages are relaxed
        */

        JwtFilter jwtFilter = new JwtFilter(providerManager(), jwtHandler, objectMapper);
        JwtValidator jwtValidator = new JwtValidator(jwtHandler, providerManager());
        JwtRefreshFilter jwtRefreshFilter = new JwtRefreshFilter(jwtHandler, providerManager());

        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-auth/login", "/api-auth/register", "/v3/api-docs/**", "/swagger-ui/**",   "/swagger-ui/index.html").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidator, JwtFilter.class)
                .addFilterAfter(jwtRefreshFilter, JwtValidator.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /*
        --> create a DAOAuthenticationProvider bean
        --> This bean is responsible for accessing the user details from database
     */

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(authProvider);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }

    /*
        --> create a JwtAuthenticationProvider bean
        --> This bean is responsible for accessing the userDetails from the jwt token
     */


    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider(jwtHandler, authProvider);
    }

    @Bean
    public ProviderManager providerManager(){
        return new ProviderManager(List.of(daoAuthenticationProvider(),jwtAuthenticationProvider()));
    }

}
