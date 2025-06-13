package com.pd.BookingSystem.features.user.controller;

import com.pd.BookingSystem.dto.AuthenticationRequest;
import com.pd.BookingSystem.dto.AuthenticationResponse;
import com.pd.BookingSystem.features.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    UserAuthenticationService userAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return userAuthenticationService.authenticate(request);
    }
}
