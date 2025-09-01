package com.myreflectionthoughts.auth.datamodel.request;

import lombok.Data;

@Data
public class RegistrationModel {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
