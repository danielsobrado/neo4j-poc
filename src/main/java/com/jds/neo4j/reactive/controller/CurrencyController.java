package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.service.CurrencyServiceImpl;
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
    private final CurrencyServiceImpl currencyService;

    @GetMapping
    public Flux<CurrencyNode> getAllCurrencies() {
        log.debug("Getting all currencies");
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{code}")
    public Mono<CurrencyNode> getCurrencyByCode(@PathVariable("code") String code) {
        log.debug("Getting currency by code: {}", code);
        return currencyService.getCurrencyByCode(code);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CurrencyNode> createCurrency(@RequestBody CurrencyNode currencyNode) {
        log.debug("Creating currency: {}", currencyNode);
        return currencyService.createCurrency(currencyNode);
    }

    @PutMapping("/{code}")
    public Mono<CurrencyNode> updateCurrency(@PathVariable("code") String code, @RequestBody CurrencyNode currencyNode) {
        log.debug("Updating currency with code: {}, data: {}", code, currencyNode);
        return currencyService.updateCurrency(code, currencyNode);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCurrency(@PathVariable("code") String code) {
        log.debug("Deleting currency with code: {}", code);
        return currencyService.deleteCurrency(code);
    }
}
