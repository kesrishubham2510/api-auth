package com.myreflectionthoughts.auth.utility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class AppUtil {


    public static String loadMessageBody(String filename)
            throws IOException {
       byte[] content = new ClassPathResource("response/"+filename).getInputStream().readAllBytes();
       return new String(content);
    }

    public static String handleMessage(String exceptionMessage){

        String message = "";

        if(StringUtils.contains(exceptionMessage, "expired")){
            message = "The JWT token has expired, please create a new one using /api-auth/refresh-token";
        }else if(StringUtils.contains(exceptionMessage, "does not match")){
            message = "The JWT token has been tempered, please create a new one using /api-auth/refresh-token";
        }

        return message;
    }
}
