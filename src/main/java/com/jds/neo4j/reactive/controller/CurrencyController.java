package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/currencies")
@Slf4j
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public Flux<CurrencyNode> getAllCurrencies() {
        log.debug("Getting all currencies");
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{id}")
    public Mono<CurrencyNode> getCurrencyById(@PathVariable("id") Long id) {
        log.debug("Getting currency by id: {}", id);
        return currencyService.getCurrencyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CurrencyNode> createCurrency(@RequestBody CurrencyNode currency) {
        log.debug("Creating currency: {}", currency);
        return currencyService.createCurrency(currency);
    }

    @PutMapping("/{id}")
    public Mono<CurrencyNode> updateCurrency(@PathVariable("id") Long id, @RequestBody CurrencyNode currency) {
        log.debug("Updating currency with id: {}, data: {}", id, currency);
        return currencyService.updateCurrency(id, currency);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCurrency(@PathVariable("id") Long id) {
        log.debug("Deleting currency with id: {}", id);
        return currencyService.deleteCurrency(id);
    }
}

