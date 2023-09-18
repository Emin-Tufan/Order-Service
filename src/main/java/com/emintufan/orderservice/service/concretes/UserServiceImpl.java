package com.emintufan.orderservice.service.concretes;

import com.emintufan.orderservice.dao.user.UserRepository;
import com.emintufan.orderservice.dto.request.CreateLoginRequest;
import com.emintufan.orderservice.dto.request.CreateRegisterRequest;
import com.emintufan.orderservice.dto.response.AuthenticationResponse;
import com.emintufan.orderservice.entities.user.User;
import com.emintufan.orderservice.exception.UserAlreadyExistsException;
import com.emintufan.orderservice.mapper.ModelMapperManager;
import com.emintufan.orderservice.security.service.JwtService;
import com.emintufan.orderservice.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapperManager modelMapperManager;
    private JwtService jwtService;
    private AuthenticationManager manager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapperManager modelMapperManager, JwtService jwtService, AuthenticationManager manager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapperManager = modelMapperManager;
        this.jwtService = jwtService;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthenticationResponse userRegister(CreateRegisterRequest userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("This email is already registered!");
        }
        User user = modelMapperManager.forRequest().map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if (user != null) {
            userRepository.save(user);
        }
        var jwtToken = jwtService.generateToken(user);
        return (AuthenticationResponse.builder()
                .token(jwtToken)).build();
    }

    @Override
    public AuthenticationResponse userLogin(CreateLoginRequest loginRequest) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()
            ));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("Invalid username or password !"));

            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("User not found: " + ex.getMessage());
        }
    }
}
