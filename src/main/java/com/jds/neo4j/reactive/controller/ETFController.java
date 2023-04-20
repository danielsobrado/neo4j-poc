package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.ETFNode;
import com.jds.neo4j.reactive.service.ETFServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/etfs")
@Slf4j
@RequiredArgsConstructor
public class ETFController {
    private final ETFServiceImpl etfService;

    @GetMapping
    public Flux<ETFNode> getAllETFs() {
        log.debug("Getting all ETFs");
        return etfService.getAllETFs();
    }

    @GetMapping("/{symbol}")
    public Mono<ETFNode> getETFBySymbol(@PathVariable("symbol") String symbol) {
        log.debug("Getting ETF by symbol: {}", symbol);
        return etfService.getETFBySymbol(symbol);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ETFNode> createETF(@RequestBody ETFNode etfNode) {
        log.debug("Creating ETF: {}", etfNode);
        return etfService.createETF(etfNode);
    }

    @PutMapping("/{symbol}")
    public Mono<ETFNode> updateETF(@PathVariable("symbol") String symbol, @RequestBody ETFNode etfNode) {
        log.debug("Updating ETF with symbol: {}, data: {}", symbol, etfNode);
        return etfService.updateETF(symbol, etfNode);
    }

    @DeleteMapping("/{symbol}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteETF(@PathVariable("symbol") String symbol) {
        log.debug("Deleting ETF with symbol: {}", symbol);
        return etfService.deleteETF(symbol);
    }
}
