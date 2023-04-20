package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.ETFNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ETFService {
    Flux<ETFNode> getAllETFs();

    Mono<ETFNode> getETFBySymbol(String symbol);

    Mono<ETFNode> createETF(ETFNode etfNode);

    Mono<ETFNode> updateETF(String symbol, ETFNode etfNode);

    Mono<Void> deleteETF(String symbol);

//    ETFNode convertToNode(ETF etf);

//    ETF convertToProto(ETFNode etfNode);
}
