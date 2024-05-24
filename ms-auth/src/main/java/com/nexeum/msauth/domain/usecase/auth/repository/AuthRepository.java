package com.nexeum.msauth.domain.usecase.auth.repository;

import com.nexeum.msauth.domain.model.auth.Auth;
import com.nexeum.msauth.domain.model.jwt.Token;

import reactor.core.publisher.Mono;

public interface AuthRepository {
    Mono<String> login(Auth auth);
    Mono<String> register(Auth auth);
    Mono<Boolean> validateJwt(Token token);
    Mono<String> getEmailFromJwt(Token token);
    Mono<String> getUserData(Auth auth);
}