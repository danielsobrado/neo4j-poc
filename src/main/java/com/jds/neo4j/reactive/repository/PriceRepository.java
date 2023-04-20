package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.PriceNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends BaseRepository<PriceNode, Long>, ReactiveNeo4jRepository<PriceNode, Long> {
}
