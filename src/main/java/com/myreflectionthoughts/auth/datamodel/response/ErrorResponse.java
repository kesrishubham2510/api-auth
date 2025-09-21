package com.myreflectionthoughts.auth.datamodel.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String key;
    private String message;
}
