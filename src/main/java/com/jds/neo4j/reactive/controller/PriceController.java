package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.PriceNode;
import com.jds.neo4j.reactive.service.PriceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PriceServiceImpl priceService;

    @Autowired
    public PriceController(PriceServiceImpl priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public Flux<PriceNode> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/{id}")
    public Mono<PriceNode> getPriceById(@PathVariable Long id) {
        return priceService.getPriceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PriceNode> createPrice(@RequestBody PriceNode price) {
        return priceService.createPrice(price);
    }

    @PutMapping("/{id}")
    public Mono<PriceNode> updatePrice(@PathVariable Long id, @RequestBody PriceNode price) {
        return priceService.updatePrice(id, price);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePrice(@PathVariable Long id) {
        return priceService.deletePrice(id);
    }
}

