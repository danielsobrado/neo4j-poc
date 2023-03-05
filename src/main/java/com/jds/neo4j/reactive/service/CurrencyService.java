package com.jds.neo4j.reactive.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.model.CurrencyProto.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
    Flux<CurrencyNode> getAllCurrencies();

    Mono<CurrencyNode> getCurrencyById(Long id);

    Mono<CurrencyNode> createCurrency(Currency currency);

    Mono<CurrencyNode> createCurrency(String currencyJson) throws InvalidProtocolBufferException;

    Mono<CurrencyNode> updateCurrency(Long id, CurrencyNode currency);

    Mono<CurrencyNode> updateCurrency(Long id, String currencyJson) throws InvalidProtocolBufferException;

    Mono<Void> deleteCurrency(Long id);
}
