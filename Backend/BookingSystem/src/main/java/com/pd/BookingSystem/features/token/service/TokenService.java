package com.pd.BookingSystem.features.token.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pd.BookingSystem.config.JwtService;
import com.pd.BookingSystem.dto.AuthenticationResponse;
import com.pd.BookingSystem.exceptions.CustomExceptions.ResourceNotFoundException;
import com.pd.BookingSystem.features.token.entity.Token;
import com.pd.BookingSystem.features.token.enums.TokenType;
import com.pd.BookingSystem.features.token.repository.TokenRepository;
import com.pd.BookingSystem.features.user.entity.User;
import com.pd.BookingSystem.features.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    public void saveUserToken(User user, String jwtToken) {
        // Logic to save user token
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            token.setExpiredAt(new java.util.Date(System.currentTimeMillis()));
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public void createRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) return;

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            Optional<User> userCheck = userRepository.findByEmail(userEmail);

            if (userCheck.isEmpty()) throw new ResourceNotFoundException("User not found");


            var user = userCheck.get();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    //Scheduled to run once at the end of each month
    @Scheduled(cron = "0 59 23 L * ?")
    @Transactional
    public int deleteExpiredTokens() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo = calendar.getTime();
        return tokenRepository.deleteAllByExpiredAtBefore(thirtyDaysAgo);
    }

}
