package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.SpinoffNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpinoffService {
    Flux<SpinoffNode> getAllSpinoffs();

    Mono<SpinoffNode> getSpinoffById(String id);

    Mono<SpinoffNode> createSpinoff(SpinoffNode spinoffNode);

    Mono<SpinoffNode> updateSpinoff(String id, SpinoffNode spinoffNode);

    Mono<Void> deleteSpinoff(String id);

//    SpinoffNode corenvertToNode(SpinoffProto.Spinoff spinoff);

}
