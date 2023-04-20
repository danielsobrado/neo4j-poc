package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.IndexNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends BaseRepository<IndexNode, String>, ReactiveNeo4jRepository<IndexNode, String> {
}
