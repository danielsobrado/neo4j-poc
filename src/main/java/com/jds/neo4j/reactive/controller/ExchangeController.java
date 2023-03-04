package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchanges")
@Slf4j
@RequiredArgsConstructor
public class ExchangeController {
    
    private final ExchangeService exchangeService;

    @GetMapping
    public Flux<ExchangeNode> getAllExchanges() {
        log.info("Retrieving all exchanges");
        return exchangeService.getAllExchanges();
    }

    @GetMapping("/{id}")
    public Mono<ExchangeNode> getExchangeById(@PathVariable Long id) {
        log.info("Retrieving exchange with id {}", id);
        return exchangeService.getExchangeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ExchangeNode> createExchange(@RequestBody ExchangeNode exchange) {
        log.info("Creating exchange {}", exchange);
        return exchangeService.createExchange(exchange);
    }

    @PutMapping("/{id}")
    public Mono<ExchangeNode> updateExchange(@PathVariable Long id, @RequestBody ExchangeNode exchange) {
        log.info("Updating exchange with id {}", id);
        return exchangeService.updateExchange(id, exchange);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteExchange(@PathVariable Long id) {
        log.info("Deleting exchange with id {}", id);
        return exchangeService.deleteExchange(id);
    }
}
