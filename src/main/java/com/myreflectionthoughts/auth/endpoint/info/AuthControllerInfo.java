package com.myreflectionthoughts.auth.endpoint.info;

import com.myreflectionthoughts.auth.config.RestConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(RestConstant.API_PREFIX)
@RestController
public class AuthControllerInfo {

    @GetMapping("/info")
    public ResponseEntity<String> getStatus(){
        return ResponseEntity.ok("API is up !");
    }
}
