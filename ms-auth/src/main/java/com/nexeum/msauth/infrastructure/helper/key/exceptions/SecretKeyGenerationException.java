package com.nexeum.msauth.infrastructure.helper.key.exceptions;

public class SecretKeyGenerationException extends RuntimeException {
    public SecretKeyGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
