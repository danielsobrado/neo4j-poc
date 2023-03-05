package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.PriceNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PriceService {
    Flux<PriceNode> getAllPrices();

    Mono<PriceNode> getPriceById(Long id);

    Mono<PriceNode> createPrice(PriceNode price);

    Mono<PriceNode> updatePrice(Long id, PriceNode price);

    Mono<Void> deletePrice(Long id);
}
