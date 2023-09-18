package com.emintufan.orderservice.service.abstracts;

import com.emintufan.orderservice.dto.request.CreateLoginRequest;
import com.emintufan.orderservice.dto.request.CreateRegisterRequest;
import com.emintufan.orderservice.dto.response.AuthenticationResponse;

public interface UserService {
    AuthenticationResponse userRegister(CreateRegisterRequest userRequest);
    AuthenticationResponse userLogin(CreateLoginRequest loginRequest);
}
