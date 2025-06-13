package com.pd.BookingSystem.features.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pd.BookingSystem.config.JwtService;
import com.pd.BookingSystem.dto.AuthenticationRequest;
import com.pd.BookingSystem.dto.AuthenticationResponse;
import com.pd.BookingSystem.exceptions.CustomExceptions.BadRequestException;
import com.pd.BookingSystem.exceptions.CustomExceptions.ValidationException;
import com.pd.BookingSystem.features.token.service.TokenService;
import com.pd.BookingSystem.features.user.entity.User;
import com.pd.BookingSystem.features.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserAuthenticationService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );


            var user = (User) authentication.getPrincipal();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            tokenService.revokeAllUserTokens(user);
            tokenService.saveUserToken(user, jwtToken);
            return ResponseEntity.ok(
                    AuthenticationResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .build()
            );
        } catch (Exception e) {
            throw new BadRequestException("Invalid credentials");
        }

    }

    public void issueRefreshToken(HttpServletRequest request,
                                  HttpServletResponse response)throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                tokenService.revokeAllUserTokens(user);
                tokenService.saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
