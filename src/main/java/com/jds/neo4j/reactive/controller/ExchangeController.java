package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.service.ExchangeServiceImpl;
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

    private final ExchangeServiceImpl exchangeService;

    @GetMapping
    public Flux<ExchangeNode> getAllExchanges() {
        log.info("Retrieving all exchanges");
        return exchangeService.getAllExchanges();
    }

    @GetMapping("/{code}")
    public Mono<ExchangeNode> getExchangeById(@PathVariable String code) {
        log.info("Retrieving exchange with code {}", code);
        return exchangeService.getExchangeById(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ExchangeNode> createExchange(@RequestBody ExchangeNode exchange) {
        log.info("Creating exchange {}", exchange);
        return exchangeService.createExchange(exchange);
    }

    @PutMapping("/{code}")
    public Mono<ExchangeNode> updateExchange(@PathVariable String code, @RequestBody ExchangeNode exchange) {
        log.info("Updating exchange with code {}", code);
        return exchangeService.updateExchange(code, exchange);
    }

    @DeleteMapping("/{code}")
    public Mono<Void> deleteExchange(@PathVariable String code) {
        log.info("Deleting exchange with code {}", code);
        return exchangeService.deleteExchange(code);
    }
}
