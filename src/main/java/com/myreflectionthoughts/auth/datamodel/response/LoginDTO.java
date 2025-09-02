package com.myreflectionthoughts.auth.datamodel.response;

import lombok.Data;

@Data
public class LoginDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private String joined;
    private String token;
    private boolean emailVerified;
}
