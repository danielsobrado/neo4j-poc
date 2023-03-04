package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.TradeNode;
import org.neo4j.driver.internal.shaded.reactor.core.publisher.Flux;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends ReactiveNeo4jRepository<TradeNode, Long> {
    Flux<TradeNode> findBySymbol(String symbol);
}
