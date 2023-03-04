package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.TradeNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends ReactiveNeo4jRepository<TradeNode, Long> {
}
