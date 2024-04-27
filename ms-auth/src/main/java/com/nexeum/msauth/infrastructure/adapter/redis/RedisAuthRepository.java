package com.nexeum.msauth.infrastructure.adapter.redis;

import com.nexeum.msauth.domain.model.auth.Auth;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisAuthRepository extends ReactiveCrudRepository<Auth, String> {

}