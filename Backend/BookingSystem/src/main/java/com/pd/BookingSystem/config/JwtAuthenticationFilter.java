package com.pd.BookingSystem.config;

import com.pd.BookingSystem.constants.Constants;
import com.pd.BookingSystem.features.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired JwtService jwtService;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull  HttpServletResponse response,
            @NonNull  FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        final String id;

        if (request.getServletPath().contains(Constants.API_VERSION + "/auth/refresh-token") ||
                request.getServletPath().contains(Constants.API_VERSION + "/auth/login") ||
                request.getServletPath().contains(Constants.API_VERSION + "/auth/register") ||
                request.getServletPath().contains(Constants.API_VERSION + "/refresh-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        id = jwtService.extractUserId(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                request.setAttribute("userId", id);
                Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Collection<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = jwtService.extractAllClaims(token);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (claims.get("roles") instanceof List) {
            List<?> roles = (List<?>) claims.get("roles");

            for (Object role : roles) {
                if (role instanceof Map) {
                    Map<?, ?> roleMap = (Map<?, ?>) role;
                    if (roleMap.containsKey("authority")) {
                        String authority = roleMap.get("authority").toString();
                        // Optionally add ROLE_ prefix if your security config expects it
                        // authority = "ROLE_" + authority;
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }
                }
            }
        }
        return authorities;
    }
}


