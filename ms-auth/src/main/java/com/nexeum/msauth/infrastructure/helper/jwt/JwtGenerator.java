package com.nexeum.msauth.infrastructure.helper.jwt;

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

    private static final Logger log = LoggerFactory.getLogger(JwtGenerator.class);

    public JwtGenerator() {
        this.secretKey = Keys.hmacShaKeyFor(new SecretKeyGenerator().generateSecretKey().getBytes());
    }

    public String generateJwt(String email) {
        long expirationTime = 1000L * 60 * 60 * 24;

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

            Date expiration = claimsJws.getPayload().getExpiration();
            log.info("Token expiration: {}", expiration);
            boolean isValid = expiration != null && expiration.after(new Date());
            log.info(isValid ? "Token is valid" : "Token is expired");
            return isValid;
        } catch (JwtException e) {
            log.error("Error validating token", e);
            return false;
        }
    }

    public String getEmailFromJwt(String token) {
        try {
            log.info("Getting email from token");

            token = token.trim();

            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error parsing JWT", e);
            return null;
        }
    }
}