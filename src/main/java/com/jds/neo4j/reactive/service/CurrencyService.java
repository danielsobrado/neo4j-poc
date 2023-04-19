package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.model.CurrencyProto.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
    Flux<CurrencyNode> getAllCurrencies();

    Mono<CurrencyNode> getCurrencyByCode(String code);

    Mono<CurrencyNode> createCurrency(CurrencyNode currencyNode);

    Mono<CurrencyNode> updateCurrency(String code, CurrencyNode currencyNode);

    Mono<Void> deleteCurrency(String code);

    CurrencyNode convertToNode(Currency currency);

    Currency convertToProto(CurrencyNode currencyNode);
}
