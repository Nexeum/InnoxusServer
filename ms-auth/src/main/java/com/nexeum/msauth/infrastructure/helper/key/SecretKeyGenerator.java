package com.nexeum.msauth.infrastructure.helper.key;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.nexeum.msauth.infrastructure.helper.key.exceptions.SecretKeyGenerationException;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecretKeyGenerator {

    public String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new SecretKeyGenerationException("Error generating secret key", e);
        }
    }
}