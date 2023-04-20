package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.Spinoff;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpinoffRepository extends ReactiveNeo4jRepository<Spinoff, Long>, BaseRepository<Spinoff, Long> {
}
