package com.nexeum.mspackage.infrastructure.adapter.redis;

import com.nexeum.mspackage.domain.model.bucket.Bucket;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RedisBucketRepository extends ReactiveCrudRepository<Bucket, String> {

}
