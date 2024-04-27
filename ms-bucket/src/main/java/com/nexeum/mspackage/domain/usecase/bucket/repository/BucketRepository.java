package com.nexeum.mspackage.domain.usecase.bucket.repository;

import com.nexeum.mspackage.domain.model.bucket.Bucket;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BucketRepository {
    Mono<Object> createBucket(Bucket bucket);
    Mono<Bucket> getBucket(String name);
    Mono<String> updateBucket(Bucket bucket);
    Mono<String> deleteBucket(String name);
    Mono<String> uploadFilesToBucket(String name, List<String> files);
}
