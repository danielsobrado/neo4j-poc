package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.TradeNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TradeService {
    Flux<TradeNode> getAllTrades();

    Mono<TradeNode> createTradeFromString(String tradeString);

    Mono<TradeNode> createTrade(TradeNode tradeNode);

    Mono<TradeNode> getTradeById(Long id);

    Mono<TradeNode> updateTrade(Long id, TradeNode tradeNode);

    Mono<Void> deleteTrade(Long id);

}
