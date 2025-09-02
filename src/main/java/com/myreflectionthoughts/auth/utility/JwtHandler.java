package com.myreflectionthoughts.auth.utility;

import com.myreflectionthoughts.auth.datamodel.entity.User;
import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtHandler {

    private final int expiryMinutes = 10;
    private final String keyString = "this i smy key to sign my JWT token";
    private final Key key = Keys.hmacShaKeyFor(keyString.getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiryMinutes * 60*100))
                .signWith(key)
                .compact();
    }

    public String validateToken(String token){
        return "true";
    }
}
