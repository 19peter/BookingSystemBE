package com.pd.BookingSystem.config.WebSocketConfig;

import com.pd.BookingSystem.config.JwtService;
import com.pd.BookingSystem.exceptions.CustomExceptions.ForbiddenException;
import com.pd.BookingSystem.exceptions.CustomExceptions.UnauthorizedException;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class HandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    JwtService jwtService;

    @Nonnull
    @Override
    protected Principal determineUser(@Nonnull ServerHttpRequest request,
                                      @Nonnull WebSocketHandler wsHandler,
                                      @Nonnull Map<String, Object> attributes) {

        String token = jwtService.getTokenFromRequest(request);
        if (jwtService.isTokenValidForHandshake(token)) {
            String id = jwtService.extractUserId(token);
            attributes.put("id", id); // Save to session
            return () -> id; // This sets Principal.getName()
        }
        throw new ForbiddenException("Invalid token for connection");
    }



}


