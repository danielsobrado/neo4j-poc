package com.jds.neo4j.reactive.repository;

import reactor.core.publisher.Mono;

public interface BaseRepository<T, ID> {
    Mono<Void> saveWithRetry(T entity);
}
