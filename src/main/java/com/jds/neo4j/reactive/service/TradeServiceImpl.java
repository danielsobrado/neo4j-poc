package com.jds.neo4j.reactive.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.TradeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    @NonNull
    private final TradeRepository tradeRepository;

    @NonNull
    private final TickerService tickerService;

    @NonNull
    private final ObjectMapper objectMapper;

    @Override
    public Mono<TradeNode> createTradeFromString(String tradeString) {
        log.debug("Creating trade from string: {}", tradeString);

        try {
            TradeNode tradeNode = objectMapper.readValue(tradeString, TradeNode.class);
            return createTrade(tradeNode);
        } catch (IOException e) {
            log.error("Failed to create trade from string: {}", tradeString, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<TradeNode> createTrade(TradeNode tradeNode) {
        log.debug("Creating trade: {}", tradeNode);

        return tickerService.getTickerBySymbol(tradeNode.getTicker().getSymbol())
                .flatMap(tickerNode -> {
                    tradeNode.setTicker(tickerNode);
                    return tradeRepository.save(tradeNode);
                });
    }


    @Override
    public Flux<TradeNode> getAllTrades() {
        log.debug("Getting all trades");

        return tradeRepository.findAll();
    }

    @Override
    public Mono<TradeNode> getTradeById(Long id) {
        log.debug("Getting trade by id: {}", id);

        return tradeRepository.findById(id);
    }

    @Override
    public Mono<TradeNode> updateTrade(Long id, TradeNode tradeNode) {
        log.debug("Updating trade with id: {}", id);

        return tradeRepository.findById(id)
                .flatMap(existingTrade -> {
                    existingTrade.setPrice(tradeNode.getPrice());
                    existingTrade.setQuantity(tradeNode.getQuantity());
                    existingTrade.setSide(tradeNode.getSide());
                    existingTrade.setTimestamp(tradeNode.getTimestamp());
                    existingTrade.setTicker(tradeNode.getTicker());
                    if (tradeNode.getSide() == TradeProto.Side.BUY) {
                        existingTrade.setBuyer(tradeNode.getBuyer());
                    } else {
                        existingTrade.setSeller(tradeNode.getSeller());
                    }
                    return tradeRepository.save(existingTrade);
                });
    }

    @Override
    public Mono<Void> deleteTrade(Long id) {
        log.debug("Deleting trade with id: {}", id);
        return tradeRepository.deleteById(id);
    }

}
