package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.TickerNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends ReactiveNeo4jRepository<TickerNode, Long> {

}
