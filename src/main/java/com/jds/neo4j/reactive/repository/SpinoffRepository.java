package com.jds.neo4j.reactive.graphs.repositories;

import com.jds.neo4j.reactive.graphs.model.SpinoffNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpinoffRepository extends ReactiveNeo4jRepository<SpinoffNode, String> {
}
