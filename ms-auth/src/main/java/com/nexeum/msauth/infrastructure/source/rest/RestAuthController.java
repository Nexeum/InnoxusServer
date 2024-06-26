package com.nexeum.msauth.infrastructure.source.rest;

import com.nexeum.msauth.domain.model.auth.Auth;
import com.nexeum.msauth.domain.model.jwt.Token;
import com.nexeum.msauth.domain.usecase.auth.repository.AuthRepository;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
@RestController
@RequestMapping("/v1/auth")
public class RestAuthController {

    private final AuthRepository authRepository;
    private final HealthEndpoint healthEndpoint;

    public RestAuthController(AuthRepository authRepository, HealthEndpoint healthEndpoint) {
        this.authRepository = authRepository;
        this.healthEndpoint = healthEndpoint;
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<Object>> health() {
        return Mono.just(ResponseEntity.ok(healthEndpoint.health()));
    }


    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody Auth auth) {
        return authRepository.register(auth)
                .map(message -> ResponseEntity.ok().body(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody Auth auth) {
        return authRepository.login(auth)
                .map(jwt -> ResponseEntity.ok().body(jwt))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage())));
    }
    @PostMapping("/validate")
    public Mono<ResponseEntity<Boolean>> validateJwt(@RequestBody Token token) {
        return authRepository.validateJwt(token)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/email")
    public Mono<ResponseEntity<String>> getEmailFromJwt(@RequestBody Token token) {
        return authRepository.getEmailFromJwt(token)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage())));
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<String>> getUserData(@RequestBody Auth auth) {
        return authRepository.getUserData(auth)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage())));
    }
}
