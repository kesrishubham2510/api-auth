package com.myreflectionthoughts.auth.usecase.registration;

import com.myreflectionthoughts.auth.datamodel.request.RegistrationModel;
import com.myreflectionthoughts.auth.datamodel.response.RegistrationDTO;

public interface Register {
    RegistrationDTO registerUser(RegistrationModel registrationModel);
}
