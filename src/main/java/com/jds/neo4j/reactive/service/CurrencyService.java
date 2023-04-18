package com.jds.neo4j.reactive.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.model.CurrencyProto.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
    Flux<CurrencyNode> getAllCurrencies();

    Mono<CurrencyNode> getCurrencyById(String id);

    Mono<CurrencyNode> createCurrency(String currencyJson) throws InvalidProtocolBufferException;

    Mono<CurrencyNode> updateCurrency(String id, CurrencyNode currency);

    Mono<CurrencyNode> updateCurrency(String id, String currencyJson) throws InvalidProtocolBufferException;

    Mono<Void> deleteCurrency(String id);

    CurrencyNode convertToNode(Currency currency);

    Currency convertToProto(CurrencyNode currencyNode);
}
