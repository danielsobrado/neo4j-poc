package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.IndexNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndexService {
    Flux<IndexNode> getAllIndexes();

    Mono<IndexNode> getIndexBySymbol(String symbol);

    Mono<IndexNode> createIndex(IndexNode indexNode);

    Mono<IndexNode> updateIndex(String symbol, IndexNode indexNode);

    Mono<Void> deleteIndex(String symbol);

//    IndexNode convertToNode(Index Indexindex);

//    Index convertToProto(IndexNode indexNode);
}
