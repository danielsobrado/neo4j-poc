package com.jds.neo4j.reactive.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BaseRepository<T, ID> {
    Mono<T> saveWithRetry(T entity);

    Flux<T> saveAllWithRetry(List<T> entities);
}
