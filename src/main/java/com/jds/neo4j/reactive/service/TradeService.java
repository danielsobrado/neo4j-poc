package com.jds.neo4j.reactive.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.TradeProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TradeService {
    Flux<TradeNode> getAllTrades();

    Mono<TradeNode> getTradeById(Long id);

    Mono<TradeNode> createTrade(String tradeJson) throws InvalidProtocolBufferException;

    Mono<TradeNode> createTrade(TradeProto.Trade tradeProto);

    Mono<TradeNode> updateTrade(Long id, String tradeJson) throws InvalidProtocolBufferException;

    Mono<TradeNode> updateTrade(Long id, TradeProto.Trade tradeProto);

    Mono<Void> deleteTrade(Long id);
}
