package com.coffeeteam.tutuplapak.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coffeeteam.tutuplapak.auth.UserClaim;
import com.coffeeteam.tutuplapak.auth.dto.AuthResponseBody;
import com.coffeeteam.tutuplapak.auth.dto.EmailAuthRequestBody;
import com.coffeeteam.tutuplapak.auth.dto.PhoneAuthRequestBody;
import com.coffeeteam.tutuplapak.auth.repository.AuthRepository;
import com.coffeeteam.tutuplapak.auth.security.JwtUtil;
import com.coffeeteam.tutuplapak.core.entity.User;
import com.coffeeteam.tutuplapak.core.exceptions.ConflictException;
import com.coffeeteam.tutuplapak.core.exceptions.InvalidCredentialsException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthService {
    
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponseBody emailRegister(EmailAuthRequestBody requestBody) throws ConflictException {
        if (authRepository.existsByEmail(requestBody.getEmail()))
            throw new ConflictException();
        
        String encryptedPass = passwordEncoder.encode(requestBody.getPassword());
        User newUser = new User();
        newUser.setEmail(requestBody.getEmail());
        newUser.setPassword(encryptedPass);

        User savedUser = authRepository.save(newUser);
        String token = jwtUtil.generateToken(new UserClaim(
            savedUser.getId(),
            savedUser.getPhone(),
            savedUser.getEmail() 
        ));
        return new AuthResponseBody(
            savedUser.getEmail(),
            savedUser.getPhone() != null ? savedUser.getPhone() : "",
            token
        );
    }

    public AuthResponseBody emailLogin(EmailAuthRequestBody requestBody) throws InvalidCredentialsException {
        User authUser = authRepository
                        .findByEmail(requestBody.getEmail())
                        .orElseThrow(() -> new EntityNotFoundException("user not found"));
        if (!passwordEncoder.matches(requestBody.getPassword(), authUser.getPassword())) throw new InvalidCredentialsException();
        String token = jwtUtil.generateToken(new UserClaim(
            authUser.getId(),
            authUser.getPhone(),
            authUser.getEmail() 
        ));
        return new AuthResponseBody(
            authUser.getEmail(),
            authUser.getPhone() != null ? authUser.getPhone() : "",
            token
        );
    }

    public AuthResponseBody phoneRegister(PhoneAuthRequestBody requestBody) throws ConflictException {
        if (authRepository.existsByPhone(requestBody.getPhone()))
            throw new ConflictException();
        
        String encryptedPass = passwordEncoder.encode(requestBody.getPassword());
        User newUser = new User();
        newUser.setPhone(requestBody.getPhone());
        newUser.setPassword(encryptedPass);

        User savedUser = authRepository.save(newUser);
        String token = jwtUtil.generateToken(new UserClaim(
            savedUser.getId(),
            savedUser.getPhone(),
            savedUser.getEmail() 
        ));
        return new AuthResponseBody(
            savedUser.getEmail() != null ? savedUser.getEmail() : "",
            savedUser.getPhone(),
            token
        );
    }

    public AuthResponseBody phoneLogin(PhoneAuthRequestBody requestBody) throws InvalidCredentialsException {
        User authUser = authRepository
                        .findByPhone(requestBody.getPhone())
                        .orElseThrow(() -> new EntityNotFoundException());
        if (!passwordEncoder.matches(requestBody.getPassword(), authUser.getPassword())) throw new InvalidCredentialsException();
        String token = jwtUtil.generateToken(new UserClaim(
            authUser.getId(),
            authUser.getPhone(),
            authUser.getEmail() 
        ));
        return new AuthResponseBody(
            authUser.getEmail() != null ? authUser.getEmail() : "",
            authUser.getPhone(),
            token
        );
    }

    public User getUserByEmailOrUsername(String email, String phone) throws EntityNotFoundException {
        if (email != null && phone != null)
            return authRepository.findByPhoneAndEmail(phone, email).orElseThrow(EntityNotFoundException::new);
        if (email != null)
            return authRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return authRepository.findByPhone(phone).orElseThrow(EntityNotFoundException::new);
    }

}
