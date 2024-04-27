package com.nexeum.mspackage.domain.usecase.bucket;

import com.nexeum.mspackage.domain.model.bucket.Bucket;
import com.nexeum.mspackage.domain.usecase.bucket.repository.BucketRepository;
import com.nexeum.mspackage.infrastructure.adapter.mongo.MongoBucketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class BucketService implements BucketRepository {

    private static final Logger log = LoggerFactory.getLogger(BucketService.class);
    private final MongoBucketRepository mongoBucketRepository;

    public BucketService(MongoBucketRepository mongoBucketRepository) {
        this.mongoBucketRepository = mongoBucketRepository;
    }

    @Override
    public Mono<Object> createBucket(Bucket bucket) {
        return mongoBucketRepository.findByName(bucket.getName())
                .flatMap(existingBucket -> {
                    log.error("Bucket already exists: {}", bucket.getName());
                    return Mono.error(new IllegalArgumentException("Bucket already exists"));
                })
                .switchIfEmpty(mongoBucketRepository.save(bucket).thenReturn("Bucket created"));
    }

    @Override
    public Mono<Bucket> getBucket(String name) {
        return mongoBucketRepository.findByName(name)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Bucket not found")));
    }

    @Override
    public Mono<String> updateBucket(Bucket bucket) {
        return mongoBucketRepository.findByName(bucket.getName())
                .flatMap(existingBucket -> {
                    log.info("Bucket found: {}", bucket.getName());
                    return mongoBucketRepository.save(existingBucket).thenReturn("Bucket updated");
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Bucket not found")));
    }

    @Override
    public Mono<String> deleteBucket(String name) {
        return mongoBucketRepository.findByName(name)
                .flatMap(existingBucket -> {
                    return mongoBucketRepository.delete(existingBucket).thenReturn("Bucket deleted");
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Bucket not found")));
    }

    @Override
    public Mono<String> uploadFilesToBucket(String name, List<String> files) {
        return mongoBucketRepository.findByName(name)
                .flatMap(existingBucket -> {
                    existingBucket.getFiles().addAll(files);
                    return mongoBucketRepository.save(existingBucket).thenReturn("Files uploaded to bucket");
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Bucket not found")));
    }
}
