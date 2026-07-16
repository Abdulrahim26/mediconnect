package com.mediconnect.mediconnectapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 🔍 DEBUG: Log the Authorization header
        System.out.println("Authorization Header: " + authHeader);

        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No Bearer token found in Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // 🔍 DEBUG: Log the extracted JWT
        System.out.println("JWT: " + jwt);

        userEmail = jwtService.extractEmail(jwt);

        // 🔍 DEBUG: Log the extracted email
        System.out.println("Email from token: " + userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // 🔍 DEBUG: Log the loaded user
            System.out.println("User loaded: " + userDetails.getUsername());
            System.out.println("Authorities: " + userDetails.getAuthorities());

            if (jwtService.isTokenValid(jwt)) {

                // 🔍 DEBUG: Log before setting authentication
                System.out.println("✅ JWT is valid. Setting SecurityContext...");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("✅ Authentication set successfully for: " + userEmail);
            } else {
                System.out.println("❌ JWT is INVALID for: " + userEmail);
            }
        } else {
            if (userEmail == null) {
                System.out.println("❌ Could not extract email from JWT");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("ℹ️ SecurityContext already has authentication: " +
                        SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }

        filterChain.doFilter(request, response);
    }
}