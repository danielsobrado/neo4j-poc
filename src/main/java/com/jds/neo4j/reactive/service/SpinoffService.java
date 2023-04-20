package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.Spinoff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpinoffService {
    Flux<Spinoff> getAllSpinoffs();

    Mono<Spinoff> getSpinoffById(Long id);

    Mono<Spinoff> createSpinoff(Spinoff spinoff);

    Mono<Spinoff> updateSpinoff(Long id, Spinoff spinoff);

    Mono<Void> deleteSpinoff(Long id);
}
