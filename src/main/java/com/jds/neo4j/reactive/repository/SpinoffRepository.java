package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.SpinoffNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface SpinoffRepository extends ReactiveNeo4jRepository<SpinoffNode, String>, BaseRepository<SpinoffNode, String> {
}
