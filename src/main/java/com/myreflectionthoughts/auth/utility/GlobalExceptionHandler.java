package com.myreflectionthoughts.auth.utility;

import com.myreflectionthoughts.auth.AuthException;
import com.myreflectionthoughts.auth.datamodel.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = AuthException.class)
    public ResponseEntity<ErrorResponse> buildExceptionMessage(AuthException authException){
        ErrorResponse response = new ErrorResponse();
        response.setMessage(authException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
