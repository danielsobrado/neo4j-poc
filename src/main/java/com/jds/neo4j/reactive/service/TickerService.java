package com.jds.neo4j.reactive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.model.TickerProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TickerService {
    Flux<TickerNode> getAllTickers();

    Mono<TickerNode> getTickerById(Long id);

    Mono<TickerNode> createTicker(String tickerJson) throws InvalidProtocolBufferException, JsonProcessingException;

    Mono<TickerNode> createTicker(TickerProto.Ticker tickerProto);

    Mono<TickerNode> updateTicker(Long id, String tickerJson) throws InvalidProtocolBufferException, JsonProcessingException;

    Mono<TickerNode> updateTicker(Long id, TickerProto.Ticker tickerProto);

    Mono<Void> deleteTicker(Long id);

    TickerProto.Ticker convertToProto(TickerNode tickerNode);
}
