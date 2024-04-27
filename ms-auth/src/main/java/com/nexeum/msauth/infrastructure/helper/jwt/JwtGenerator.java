package com.nexeum.msauth.infrastructure.helper.jwt;

import com.nexeum.msauth.infrastructure.helper.key.SecretKeyGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGenerator {

    private final SecretKeyGenerator secretKeyGenerator;

    public JwtGenerator() {
        this.secretKeyGenerator = new SecretKeyGenerator();
    }

    public String generateJwt(String email) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyGenerator.generateSecretKey().getBytes());

        long expirationTime = 1000 * 60 * 60 * 24;

        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}