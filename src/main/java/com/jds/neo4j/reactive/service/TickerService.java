package com.jds.neo4j.reactive.service;


import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.repository.TickerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class TickerService {
    private final TickerRepository tickerRepository;

    public Flux<TickerNode> getAllTickers() {
        log.debug("Getting all tickers");
        return tickerRepository.findAll();
    }

    public Mono<TickerNode> getTickerById(Long id) {
        log.debug("Getting ticker by id: {}", id);
        return tickerRepository.findById(id);
    }

    public Mono<TickerNode> createTicker(TickerNode ticker) {
        log.debug("Creating ticker: {}", ticker);
        return tickerRepository.save(ticker);
    }

    public Mono<TickerNode> updateTicker(Long id, TickerNode ticker) {
        log.debug("Updating ticker with id: {}, data: {}", id, ticker);
        return tickerRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(ticker.getSymbol());
                    existing.setName(ticker.getName());
                    existing.setExchange(ticker.getExchange());
                    return existing;
                })
                .flatMap(tickerRepository::save);
    }

    public Mono<Void> deleteTicker(Long id) {
        log.debug("Deleting ticker with id: {}", id);
        return tickerRepository.deleteById(id);
    }
}

