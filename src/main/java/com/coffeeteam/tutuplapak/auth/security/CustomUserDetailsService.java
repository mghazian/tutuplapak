package com.coffeeteam.tutuplapak.auth.security;

import com.coffeeteam.tutuplapak.auth.repository.AuthRepository;
import com.coffeeteam.tutuplapak.auth.service.AuthService;
import com.coffeeteam.tutuplapak.core.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split("\\|", -1);

        String email = split.length > 0 ? (split[0].isEmpty() ? null : split[0]) : null;
        String phone = split.length > 1 ? (split[1].isEmpty() ? null : split[1]) : null;

        try {
            User user = authService.getUserByEmailOrUsername(email, phone);
            return new CustomUserDetails(user);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }

}
