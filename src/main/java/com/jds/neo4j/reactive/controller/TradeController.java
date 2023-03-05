package com.jds.neo4j.reactive.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.service.TradeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/trades")
@Slf4j
@RequiredArgsConstructor
public class TradeController {
    private final TradeServiceImpl tradeService;

    @GetMapping
    public Flux<TradeNode> getAllTrades() {
        log.debug("Getting all trades");
        return tradeService.getAllTrades();
    }

    @GetMapping("/{id}")
    public Mono<TradeNode> getTradeById(@PathVariable("id") Long id) {
        log.debug("Getting trade by id: {}", id);
        return tradeService.getTradeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TradeNode> createTrade(@RequestBody String trade) throws InvalidProtocolBufferException {
        log.debug("Creating trade: {}", trade);
        return tradeService.createTrade(trade);
    }

    @PutMapping("/{id}")
    public Mono<TradeNode> updateTrade(@PathVariable("id") Long id, @RequestBody String trade) throws InvalidProtocolBufferException {
        log.debug("Updating trade with id: {}, data: {}", id, trade);
        return tradeService.updateTrade(id, trade);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTrade(@PathVariable("id") Long id) {
        log.debug("Deleting trade with id: {}", id);
        return tradeService.deleteTrade(id);
    }
}
