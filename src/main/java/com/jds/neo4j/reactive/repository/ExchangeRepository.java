package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends BaseRepository<ExchangeNode, String>, ReactiveNeo4jRepository<ExchangeNode, String> {
}
