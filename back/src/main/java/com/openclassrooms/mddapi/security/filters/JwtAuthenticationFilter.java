package com.openclassrooms.mddapi.security.filters;

import com.openclassrooms.mddapi.security.JwtProvider;
import com.openclassrooms.mddapi.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider; // Service to manage JWT
    @Autowired
    private UserServiceImpl userServiceImpl; // Service to retrieve user information

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get the Authorization header from the request
        String authHeader = request.getHeader("Authorization");

        // Check if the header is null or does not start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response); // Pass to the next filter without authentication
            return;
        }

        // Extract the JWT token by removing "Bearer " (the first 7 characters)
        String jwt = authHeader.substring(7);
        // Extract the username from the JWT
        String email = jwtProvider.extractUsername(jwt);

        // Check that the username is valid and the user is not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user from the database
            UserDetails userDetails = userServiceImpl.loadUserByEmail(email);

            // Check if the token is valid
            if (jwtProvider.validateToken(jwt, userDetails)) {
                // Create an authentication object with the user's information
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Add the authenticated user to the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continue executing the other filters
        chain.doFilter(request, response);
    }
}