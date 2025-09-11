package com.coffeeteam.tutuplapak.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.coffeeteam.tutuplapak.auth.UserClaim;
import com.coffeeteam.tutuplapak.core.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserClaim userClaim) {
        long currentTimeMillis = System.currentTimeMillis();
        long tokenValidityMs = Duration.ofHours(7).toMillis();

        Date issuedAt = new Date(currentTimeMillis);
        Date expiration = new Date(currentTimeMillis + tokenValidityMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userClaim.getId());
        claims.put("email", userClaim.getEmail());
        claims.put("phone", userClaim.getPhone());

        return Jwts.builder()
                .subject(userClaim.getId().toString())
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public UserClaim extractClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return new UserClaim(
                Long.parseLong(claims.get("id").toString()),
                claims.get("phone").toString(),
                claims.get("email").toString()
        );
    }

    public boolean validateToken(String token, UserClaim userClaim, User user) {
        try {

            return userClaim.getId() == user.getId() 
                && userClaim.getEmail().equals(user.getEmail())
                && userClaim.getPhone().equals(user.getPhone()) 
                && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }


    private boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}