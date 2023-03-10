package com.jds.neo4j.reactive.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.model.ExchangeProto.Exchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeService {

    Flux<ExchangeNode> getAllExchanges();

    Mono<ExchangeNode> getExchangeById(Long id);

    Mono<ExchangeNode> createExchange(String exchangeJson) throws InvalidProtocolBufferException;

    Mono<ExchangeNode> createExchange(ExchangeNode exchange);

    Mono<ExchangeNode> updateExchange(Long id, ExchangeNode exchange);

    Mono<Void> deleteExchange(Long id);

    default ExchangeNode getExchangeNodeFromProto(Exchange exchange) {
        // Extract the exchange information from the Trade message
        String exchangeCode = exchange.getCode();
        String exchangeName = exchange.getName();
        String exchangeCountry = exchange.getCountry();

        // Create a new ExchangeNode from the exchange information
        return new ExchangeNode(exchangeCode, exchangeName, exchangeCountry);
    }

}
