package com.mediconnect.mediconnectapi.controller;


import com.mediconnect.mediconnectapi.dto.request.LoginRequest;
import com.mediconnect.mediconnectapi.dto.request.RegisterRequest;
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
            @Valid @RequestBody RegisterRequest request
    ){

        return ResponseEntity.ok(
                authService.register(request)
        );
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ){

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

}