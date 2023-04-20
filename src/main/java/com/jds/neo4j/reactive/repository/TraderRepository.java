package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.TraderNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepository extends BaseRepository<TraderNode, String>, ReactiveNeo4jRepository<TraderNode, String> {
}

