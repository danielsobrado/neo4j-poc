package com.jds.neo4j.reactive.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
public class BaseRepositoryImpl<T, ID> implements BaseRepository<T, ID> {

    private final ReactiveNeo4jTemplate reactiveNeo4jTemplate;

    public BaseRepositoryImpl(ReactiveNeo4jTemplate reactiveNeo4jTemplate) {
        this.reactiveNeo4jTemplate = reactiveNeo4jTemplate;
    }

    @Override
    public Mono<T> saveWithRetry(T entity) {
        return reactiveNeo4jTemplate.save(entity)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)).maxBackoff(Duration.ofSeconds(1)));
    }


    @Override
    public Flux<T> saveAllWithRetry(List<T> entities) {
        return Flux.fromIterable(entities)
                .flatMap(entity -> reactiveNeo4jTemplate.save(entity)
                        .retryWhen(Retry.backoff(3, Duration.ofMillis(100))
                                .maxBackoff(Duration.ofSeconds(1))))
                .doOnError(throwable -> log.error("Error saving entities: " + throwable.getMessage()));
    }
}
