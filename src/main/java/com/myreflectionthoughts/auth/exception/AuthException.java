package com.myreflectionthoughts.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{

    private final String key;
    public AuthException(String key, String message){
        super(message);
        this.key = key;
    }
}
