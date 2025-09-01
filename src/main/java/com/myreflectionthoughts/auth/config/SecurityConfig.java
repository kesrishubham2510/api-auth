package com.myreflectionthoughts.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-auth/**", "/v3/api-docs/**", "/swagger-ui/**",   "/swagger-ui/index.html").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

}
