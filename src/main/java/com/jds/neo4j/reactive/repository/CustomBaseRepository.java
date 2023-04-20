package com.jds.neo4j.reactive.repository;

import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public abstract class CustomBaseRepository<T, ID> implements ReactiveNeo4jRepository<T, ID> {

    private static final int MAX_RETRIES = 3;
    private final ReactiveNeo4jTemplate reactiveNeo4jTemplate;

    public CustomBaseRepository(ReactiveNeo4jTemplate reactiveNeo4jTemplate) {
        this.reactiveNeo4jTemplate = reactiveNeo4jTemplate;
    }

    public Mono<T> saveWithRetry(T object) {
        return reactiveNeo4jTemplate.save(object)
                .retryWhen(Retry.backoff(MAX_RETRIES, Duration.ofMillis(100))
                        .filter(throwable -> throwable instanceof TransientDataAccessResourceException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            return new RuntimeException("Max retries exhausted for save operation", retrySignal.failure());
                        }));
    }
}
