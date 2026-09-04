package com.mediconnect.mediconnectapi.controller;

import com.mediconnect.mediconnectapi.dto.request.ForgotPasswordRequest;
import com.mediconnect.mediconnectapi.dto.request.LoginRequest;
import com.mediconnect.mediconnectapi.dto.request.RegisterRequest;
import com.mediconnect.mediconnectapi.dto.request.ResetPasswordRequest;
import com.mediconnect.mediconnectapi.dto.response.LoginResponse;
import com.mediconnect.mediconnectapi.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ResponseEntity.ok(
                "If an account exists with this email address, a password reset link has been sent."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }
}