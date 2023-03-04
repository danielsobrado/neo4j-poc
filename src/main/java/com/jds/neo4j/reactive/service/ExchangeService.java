package com.jds.neo4j.reactive.service;


import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public Flux<ExchangeNode> getAllExchanges() {
        log.debug("Getting all exchanges");
        return exchangeRepository.findAll();
    }

    public Mono<ExchangeNode> getExchangeById(Long id) {
        log.debug("Getting exchange by id: {}", id);
        return exchangeRepository.findById(id);
    }

    public Mono<ExchangeNode> createExchange(ExchangeNode exchange) {
        log.debug("Creating exchange: {}", exchange);
        return exchangeRepository.save(exchange);
    }

    public Mono<ExchangeNode> updateExchange(Long id, ExchangeNode exchange) {
        log.debug("Updating exchange with id: {}, data: {}", id, exchange);
        return exchangeRepository.findById(id)
                .map(existing -> {
                    existing.setName(exchange.getName());
                    existing.setCountry(exchange.getCountry());
                    existing.setCode(exchange.getCode());
                    return existing;
                })
                .flatMap(exchangeRepository::save);
    }

    public Mono<Void> deleteExchange(Long id) {
        log.debug("Deleting exchange with id: {}", id);
        return exchangeRepository.deleteById(id);
    }
}





