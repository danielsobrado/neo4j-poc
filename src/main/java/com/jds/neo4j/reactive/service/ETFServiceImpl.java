package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.ETFNode;
import com.jds.neo4j.reactive.repository.ETFRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ETFServiceImpl implements ETFService {
    private final ETFRepository etfRepository;

    @Override
    public Flux<ETFNode> getAllETFs() {
        log.debug("Getting all ETFs");
        return etfRepository.findAll();
    }

    @Override
    public Mono<ETFNode> getETFBySymbol(String symbol) {
        log.debug("Getting ETF by symbol: {}", symbol);
        return etfRepository.findById(symbol);
    }

    @Override
    public Mono<ETFNode> createETF(ETFNode etfNode) {
        log.debug("Creating ETF: {}", etfNode);
        return etfRepository.saveWithRetry(etfNode);
    }

    @Override
    public Mono<ETFNode> updateETF(String symbol, ETFNode etfNode) {
        log.debug("Updating ETF with symbol: {}, data: {}", symbol, etfNode);
        return etfRepository.findById(symbol)
                .map(existing -> {
                    existing.setName(etfNode.getName());
                    existing.setComponents(etfNode.getComponents());
                    return existing;
                })
                .flatMap(etfRepository::save);
    }

    @Override
    public Mono<Void> deleteETF(String symbol) {
        log.debug("Deleting ETF with symbol: {}", symbol);
        return etfRepository.deleteById(symbol);
    }

}
