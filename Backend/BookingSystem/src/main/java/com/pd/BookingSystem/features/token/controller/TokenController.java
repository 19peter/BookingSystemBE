package com.pd.BookingSystem.features.token.controller;

import com.pd.BookingSystem.constants.Constants;
import com.pd.BookingSystem.features.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(Constants.API_VERSION + "/refresh-token")
public class TokenController {

    @Autowired
    TokenService tokenService;
    public ResponseEntity<HttpStatus> refreshToken(HttpServletRequest request,
                                                   HttpServletResponse response) throws IOException {

        try {
            tokenService.createRefreshToken(request, response);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
