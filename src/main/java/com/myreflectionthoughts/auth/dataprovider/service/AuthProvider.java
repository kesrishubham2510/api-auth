package com.myreflectionthoughts.auth.dataprovider.service;

import com.myreflectionthoughts.auth.datamodel.entity.User;
import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import com.myreflectionthoughts.auth.dataprovider.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthProvider implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthProvider(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /*
       --> User can pass email or username in the request because both are unique
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        UserAuth userDetails = null;

        User existingUserByEmail = userRepository.findByEmail(username);
        User existingUserByUsername = userRepository.findByUsername(username);

        if(!Objects.isNull(existingUserByEmail)){
            userDetails = new UserAuth(existingUserByEmail);
        }else if(!Objects.isNull(existingUserByUsername)) {
            userDetails = new UserAuth(existingUserByUsername);
        }

        return userDetails;
    }
}
