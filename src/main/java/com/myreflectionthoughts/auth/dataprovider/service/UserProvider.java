package com.myreflectionthoughts.auth.dataprovider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreflectionthoughts.auth.exception.AuthException;
import com.myreflectionthoughts.auth.datamodel.entity.User;
import com.myreflectionthoughts.auth.datamodel.entity.UserAuth;
import com.myreflectionthoughts.auth.datamodel.request.LoginModel;
import com.myreflectionthoughts.auth.datamodel.request.RegistrationModel;
import com.myreflectionthoughts.auth.datamodel.response.LoginDTO;
import com.myreflectionthoughts.auth.datamodel.response.RegistrationDTO;
import com.myreflectionthoughts.auth.datamodel.role.UserRole;
import com.myreflectionthoughts.auth.dataprovider.repository.UserRepository;
import com.myreflectionthoughts.auth.usecase.authentication.Login;
import com.myreflectionthoughts.auth.usecase.registration.Register;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserProvider implements Register {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;

    public UserProvider(UserRepository userRepository,
                        BCryptPasswordEncoder passwordEncoder,
                        ObjectMapper objectMapper)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = objectMapper;
    }

    /*
      --> The username and email will always be unique
    */
    @Override
    public RegistrationDTO registerUser(RegistrationModel registrationModel) {

        User existingUserByEmail = userRepository.findByEmail(registrationModel.getEmail());
        User existingUserByUsername = userRepository.findByUsername(registrationModel.getUsername());

        if(Objects.isNull(existingUserByEmail) && Objects.isNull(existingUserByUsername)){
            return mapToRegistrationDTO(userRepository.save(mapToUser(registrationModel)));
        }else if(Objects.nonNull(existingUserByEmail)){
            throw new AuthException("User with email:- "+registrationModel.getEmail()+" already exists");
        }else{
            throw new AuthException("User with username:- "+registrationModel.getUsername()+" already exists");
        }
    }

    private User mapToUser(RegistrationModel model){
        User user = new User();
        user.setEmail(model.getEmail());
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setUsername(model.getUsername());
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        user.setJoined(String.valueOf(Instant.now().getEpochSecond()));
        return user;
    }

    private RegistrationDTO mapToRegistrationDTO(User user){
        return mapper.convertValue(user, RegistrationDTO.class);
    }
}
