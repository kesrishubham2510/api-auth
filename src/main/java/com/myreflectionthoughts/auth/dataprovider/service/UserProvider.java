package com.myreflectionthoughts.auth.dataprovider.service;

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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserProvider implements Register, Login {

    private final UserRepository userRepository;
    private final AuthProvider authProvider;

    public UserProvider(UserRepository userRepository, AuthProvider authProvider){
        this.userRepository = userRepository;
        this.authProvider  = authProvider;
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

        if(Objects.isNull(userDetails)){
            System.out.println("No user found");
        }else{
            // match password and help with login
            if(loginModel.getPassword().equals(userDetails.getPassword())){
                return mapToLoginDTO((UserAuth) userDetails);
            }

        }
        return null;
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
        user.setPassword(model.getPassword());
        user.setJoined(String.valueOf(Instant.now().getEpochSecond()));
        return user;
    }

    private RegistrationDTO mapToRegistrationDTO(User user){
        RegistrationDTO registrationDTO = new RegistrationDTO();
        return registrationDTO;
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
