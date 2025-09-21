package com.coffeeteam.tutuplapak.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.coffeeteam.tutuplapak.auth.UserClaim;
import com.coffeeteam.tutuplapak.core.entity.User;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
                Optional.ofNullable(claims.get("phone")).map(Object::toString).orElse(null),
                Optional.ofNullable(claims.get("email")).map(Object::toString).orElse(null)
        );
    }

    public boolean validateToken(String token, UserClaim userClaim, User user) {
        return userClaim.getId().equals(user.getId()) && !isTokenExpired(token);
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