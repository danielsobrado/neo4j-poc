package com.jds.neo4j.reactive.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.service.TickerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tickers")
@Slf4j
@RequiredArgsConstructor
public class TickerController {

    private final TickerServiceImpl tickerService;

    @GetMapping
    public Flux<TickerNode> getAllTickers() {
        log.info("Retrieving all tickers");
        return tickerService.getAllTickers();
    }

    @GetMapping("/{id}")
    public Mono<TickerNode> getTickerById(@PathVariable Long id) {
        log.info("Retrieving ticker with id {}", id);
        return tickerService.getTickerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TickerNode> createTicker(@RequestBody String ticker) throws InvalidProtocolBufferException, JsonProcessingException {
        log.info("Creating ticker {}", ticker);


        return tickerService.createTicker(ticker);
    }

    @PutMapping("/{id}")
    public Mono<TickerNode> updateTicker(@PathVariable Long id, @RequestBody String ticker) throws InvalidProtocolBufferException {
        log.info("Updating ticker with id {}", id);
        return tickerService.updateTicker(id, ticker);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTicker(@PathVariable Long id) {
        log.info("Deleting ticker with id {}", id);
        return tickerService.deleteTicker(id);
    }
}
