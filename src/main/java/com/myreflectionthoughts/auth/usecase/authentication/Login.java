package com.myreflectionthoughts.auth.usecase.authentication;

import com.myreflectionthoughts.auth.datamodel.request.LoginModel;
import com.myreflectionthoughts.auth.datamodel.response.LoginDTO;

public interface Login {
    LoginDTO loginUser(LoginModel loginModel);
}
