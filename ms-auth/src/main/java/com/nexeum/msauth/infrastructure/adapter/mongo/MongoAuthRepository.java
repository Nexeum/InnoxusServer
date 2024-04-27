package com.nexeum.msauth.infrastructure.adapter.mongo;

import com.nexeum.msauth.domain.model.auth.Auth;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MongoAuthRepository extends ReactiveMongoRepository<Auth, String> {
    Mono<Auth> findByEmail(String email);
}