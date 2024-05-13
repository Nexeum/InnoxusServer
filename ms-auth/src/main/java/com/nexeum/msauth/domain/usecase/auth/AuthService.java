package com.nexeum.msauth.domain.usecase.auth;

import com.nexeum.msauth.domain.model.auth.Auth;
import com.nexeum.msauth.domain.usecase.auth.repository.AuthRepository;
import com.nexeum.msauth.infrastructure.adapter.mongo.MongoAuthRepository;
import com.nexeum.msauth.infrastructure.helper.jwt.JwtGenerator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;

@Service
public class AuthService implements AuthRepository {
    private final MongoAuthRepository mongoAuthRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final JwtGenerator jwtGenerator;

    public AuthService(MongoAuthRepository mongoAuthRepository, JwtGenerator jwtGenerator) {
        this.mongoAuthRepository = mongoAuthRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Mono<String> login(Auth auth) {
        log.info("Initiating login for email: {}", auth.getEmail());
        return mongoAuthRepository.findByEmail(auth.getEmail())
                .flatMap(existingAuth -> {
                    if (existingAuth.getEmail() == null) {
                        log.error("Email not found: {}", auth.getEmail());
                        return Mono.error(new RuntimeException("Email not found"));
                    } else if (!existingAuth.getPassword().equals(auth.getPassword())) {
                        log.error("Invalid password for email: {}", auth.getEmail());
                        return Mono.error(new RuntimeException("Invalid password"));
                    } else {
                        log.info("User logged in");
                        String jwt = jwtGenerator.generateJwt(auth.getEmail());
                        return Mono.just(jwt);
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Email not found")));
    }

    @Override
    public Mono<String> register(Auth auth) {
        log.info("Initiating registration for email: {}", auth.getEmail());
        return mongoAuthRepository.findByEmail(auth.getEmail())
                .flatMap(existingAuth -> {
                    log.error("Email already exists: {}", auth.getEmail());
                    return Mono.just("Email already exists");
                })
                .switchIfEmpty(
                    mongoAuthRepository.save(auth)
                        .doOnSuccess(result -> log.info("Registration successful for email: {}", auth.getEmail()))
                        .doOnError(e -> log.error("Error during registration for email: {}", auth.getEmail(), e))
                        .then(Mono.just("Registration successful for email: " + auth.getEmail()))
                )
                .onErrorResume(e -> {
                    log.error("Error during registration for email: {}", auth.getEmail(), e);
                    return Mono.just("Error during registration: " + e.getMessage());
                });
    }

    @Override
    public Mono<Boolean> validateJwt(String token) {
        return Mono.fromCallable(() -> jwtGenerator.validateJwt(token))
                   .onErrorReturn(false);
    }
}