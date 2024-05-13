package com.nexeum.mspackage.infrastructure.source.rest;

import com.nexeum.mspackage.domain.model.bucket.Bucket;
import com.nexeum.mspackage.domain.usecase.bucket.repository.BucketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/v1/buckets")
public class RestBucketController {

    private final BucketRepository bucketRepository;

    public RestBucketController(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Object>> createBucket(@RequestBody Bucket bucket) {
        return bucketRepository.createBucket(bucket)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/get/{name}")
    public Mono<ResponseEntity<Bucket>> getBucket(@PathVariable String name) {
        return bucketRepository.getBucket(name)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(new ResponseEntity<>(null, HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/get")
    public Mono<ResponseEntity<List<Bucket>>> getBuckets() {
        return bucketRepository.getBuckets()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<String>> updateBucket(@RequestBody Bucket bucket) {
        return bucketRepository.updateBucket(bucket)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{name}")
    public Mono<ResponseEntity<String>> deleteBucket(@PathVariable String name) {
        return bucketRepository.deleteBucket(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/files")
    public Mono<ResponseEntity<String>> uploadFilesToBucket(@PathVariable String name, @RequestBody List<String> files) {
        return bucketRepository.uploadFilesToBucket(name, files)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}