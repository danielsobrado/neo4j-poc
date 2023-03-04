package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends ReactiveNeo4jRepository<CurrencyNode, Long> {
}
