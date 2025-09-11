package com.coffeeteam.tutuplapak.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeeteam.tutuplapak.auth.dto.AuthResponseBody;
import com.coffeeteam.tutuplapak.auth.dto.EmailAuthRequestBody;
import com.coffeeteam.tutuplapak.auth.dto.PhoneAuthRequestBody;
import com.coffeeteam.tutuplapak.auth.service.AuthService;
import com.coffeeteam.tutuplapak.core.exceptions.ConflictException;
import com.coffeeteam.tutuplapak.core.exceptions.InvalidCredentialsException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @PostMapping("/register/email")
    public ResponseEntity<AuthResponseBody> emailRegister(@Valid @RequestBody EmailAuthRequestBody requestBody) throws ConflictException {

        AuthResponseBody response = this.authService.emailRegister(requestBody);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login/email")
    public ResponseEntity<AuthResponseBody> emailLogin(@Valid @RequestBody EmailAuthRequestBody request) throws InvalidCredentialsException {
        AuthResponseBody response = this.authService.emailLogin(request);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/phone")
    public ResponseEntity<AuthResponseBody> phoneRegister(@Valid @RequestBody PhoneAuthRequestBody requestBody) throws ConflictException {

        AuthResponseBody response = this.authService.phoneRegister(requestBody);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login/phone")
    public ResponseEntity<AuthResponseBody> phoneLogin(@Valid @RequestBody PhoneAuthRequestBody request) throws InvalidCredentialsException {
        AuthResponseBody response = this.authService.phoneLogin(request);
        
        return ResponseEntity.ok(response);
    }

}
