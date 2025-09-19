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
public class UserProvider implements Register, Login {

    private final UserRepository userRepository;
    private final AuthProvider authProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private ObjectMapper mapper;

    public UserProvider(UserRepository userRepository,
                        AuthProvider authProvider,
                        BCryptPasswordEncoder passwordEncoder,
                        ObjectMapper objectMapper)
    {
        this.userRepository = userRepository;
        this.authProvider  = authProvider;
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

    // first check for the user-password combination
    @Override
    public LoginDTO loginUser(LoginModel loginModel) {
        UserDetails userDetails = authProvider.loadUserByUsername(loginModel.getUsername());

        if(loginModel.getPassword().equals(userDetails.getPassword())){
            // match password and help with login
            return mapToLoginDTO((UserAuth) userDetails);
        }

        throw new AuthException("Username/password combination is wrong, please check and try again");
    }


    public User fetchMyUser(String username){
        Optional<User> optionalUser = Optional.of(userRepository.findByUsername(username));
        return optionalUser.orElseThrow(()-> new RuntimeException("User:- "+username+" does not exists"));
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

    private LoginDTO mapToLoginDTO(UserAuth userDetails){
        LoginDTO loginDTO = new LoginDTO();

        loginDTO.setUserId(userDetails.getUserId());
        loginDTO.setEmail(userDetails.getUser().getEmail());
        loginDTO.setFirstName(userDetails.getUser().getFirstName());
        loginDTO.setLastName(userDetails.getUser().getLastName());
        loginDTO.setUsername(userDetails.getUsername());
        loginDTO.setJoined(userDetails.getUser().getJoined());
        loginDTO.setRole(userDetails.getUser().getRole().name());
        loginDTO.setToken("token");

        return loginDTO;
    }
}
