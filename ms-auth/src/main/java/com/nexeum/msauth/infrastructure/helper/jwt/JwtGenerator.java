package com.nexeum.msauth.infrastructure.helper.jwt;

import com.nexeum.msauth.domain.usecase.auth.AuthService;
import com.nexeum.msauth.infrastructure.helper.key.SecretKeyGenerator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtGenerator {

    private final SecretKey secretKey;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public JwtGenerator() {
        this.secretKey = Keys.hmacShaKeyFor(new SecretKeyGenerator().generateSecretKey().getBytes());
    }

    public String generateJwt(String email) {
        long expirationTime = 1000 * 60 * 60 * 24;

        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateJwt(String token) {
        log.info("Validating token");
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            log.info("Token expiration: {}", claimsJws.getPayload().getExpiration());
            log.info(claimsJws.getPayload().getExpiration().after(new Date()) ? "Token is valid" : "Token is expired");
            return claimsJws.getPayload().getExpiration().after(new Date());
        } catch (JwtException e) {
            log.error("Error validating token", e);
            return false;
        }
    }
}