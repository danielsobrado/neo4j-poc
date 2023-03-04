package com.jds.neo4j.reactive.service;


import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;

    public Flux<TradeNode> getAllTrades() {
        log.debug("Getting all trades");
        return tradeRepository.findAll();
    }

    public Mono<TradeNode> getTradeById(Long id) {
        log.debug("Getting trade by id: {}", id);
        return tradeRepository.findById(id);
    }

    public Mono<TradeNode> createTrade(TradeNode trade) {
        log.debug("Creating trade: {}", trade);
        return tradeRepository.save(trade);
    }

    public Mono<TradeNode> updateTrade(Long id, TradeNode trade) {
        log.debug("Updating trade with id: {}, data: {}", id, trade);
        return tradeRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(trade.getSymbol());
                    existing.setPrice(trade.getPrice());
                    existing.setQuantity(trade.getQuantity());
                    existing.setSide(trade.getSide());
                    existing.setExchange(trade.getExchange());
                    existing.setTimestamp(trade.getTimestamp());
                    return existing;
                })
                .flatMap(tradeRepository::save);
    }

    public Mono<Void> deleteTrade(Long id) {
        log.debug("Deleting trade with id: {}", id);
        return tradeRepository.deleteById(id);
    }
}



