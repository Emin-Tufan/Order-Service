package com.emintufan.orderservice.controller;

import com.emintufan.orderservice.dto.request.CreateLoginRequest;
import com.emintufan.orderservice.dto.request.CreateRegisterRequest;
import com.emintufan.orderservice.dto.response.AuthenticationResponse;
import com.emintufan.orderservice.exception.UserAlreadyExistsException;
import com.emintufan.orderservice.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> UserRegister(@RequestBody CreateRegisterRequest userRequest) {

        try {
            AuthenticationResponse response = userService.userRegister(userRequest);
            URI location = URI.create("/register");
            return (ResponseEntity.created(location).body(response));
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> UserLogin(@RequestBody CreateLoginRequest loginRequest) {
        try {
            AuthenticationResponse response = userService.userLogin(loginRequest);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
