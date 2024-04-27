package com.nexeum.msauth.domain.usecase.auth.repository;

import com.nexeum.msauth.domain.model.auth.Auth;
import reactor.core.publisher.Mono;

public interface AuthRepository {
    Mono<String> login(Auth auth);
    Mono<String> register(Auth auth);
}