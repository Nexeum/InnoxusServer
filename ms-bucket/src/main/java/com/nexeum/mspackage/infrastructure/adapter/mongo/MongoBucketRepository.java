package com.nexeum.mspackage.infrastructure.adapter.mongo;

import com.nexeum.mspackage.domain.model.bucket.Bucket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoBucketRepository extends ReactiveMongoRepository<Bucket, String> {
    Mono<Bucket> findByName(String name);
}
