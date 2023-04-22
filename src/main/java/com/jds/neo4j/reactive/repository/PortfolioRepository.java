package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.PortfolioNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends BaseRepository<PortfolioNode, String>, ReactiveNeo4jRepository<PortfolioNode, String> {
}

