package com.jds.neo4j.reactive.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.model.ExchangeProto.Exchange;
import com.jds.neo4j.reactive.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeRepository exchangeRepository;

    @Override
    public Flux<ExchangeNode> getAllExchanges() {
        log.debug("Getting all exchanges");

        return exchangeRepository.findAll();
    }

    @Override
    public Mono<ExchangeNode> getExchangeById(String code) {
        log.debug("Getting exchange by code: {}", code);

        return exchangeRepository.findById(code);
    }

    @Override
    public Mono<ExchangeNode> createExchange(String exchangeJson) throws InvalidProtocolBufferException {
        log.debug("Creating exchange: {}", exchangeJson);

        // Create a new ExchangeNode from the exchange information
        ExchangeNode exchangeNode = getExchangeNodeFromProto(Exchange.parseFrom(exchangeJson.getBytes()));

        return exchangeRepository.saveWithRetry(exchangeNode);
    }

    @Override
    public Mono<ExchangeNode> createExchange(ExchangeNode exchange) {
        log.debug("Creating exchange: {}", exchange);

        return exchangeRepository.save(exchange);
    }

    @Override
    public Mono<ExchangeNode> updateExchange(String code, ExchangeNode exchange) {
        log.debug("Updating exchange with code: {}, data: {}", code, exchange);

        return exchangeRepository.findById(code)
                .map(existing -> {
                    existing.setName(exchange.getName());
                    existing.setCountry(exchange.getCountry());
                    return existing;
                })
                .flatMap(exchangeRepository::save);
    }

    @Override
    public Mono<Void> deleteExchange(String code) {
        log.debug("Deleting exchange with code: {}", code);

        return exchangeRepository.deleteById(code);
    }

}
