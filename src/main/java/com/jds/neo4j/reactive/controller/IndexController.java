package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.IndexNode;
import com.jds.neo4j.reactive.service.IndexServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/indexes")
@Slf4j
@RequiredArgsConstructor
public class IndexController {
    private final IndexServiceImpl indexService;

    @GetMapping
    public Flux<IndexNode> getAllIndexes() {
        log.debug("Getting all indexes");
        return indexService.getAllIndexes();
    }

    @GetMapping("/{symbol}")
    public Mono<IndexNode> getIndexBySymbol(@PathVariable("symbol") String symbol) {
        log.debug("Getting index by symbol: {}", symbol);
        return indexService.getIndexBySymbol(symbol);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<IndexNode> createIndex(@RequestBody IndexNode indexNode) {
        log.debug("Creating index: {}", indexNode);
        return indexService.createIndex(indexNode);
    }

    @PutMapping("/{symbol}")
    public Mono<IndexNode> updateIndex(@PathVariable("symbol") String symbol, @RequestBody IndexNode indexNode) {
        log.debug("Updating index with symbol: {}, data: {}", symbol, indexNode);
        return indexService.updateIndex(symbol, indexNode);
    }

    @DeleteMapping("/{symbol}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteIndex(@PathVariable("symbol") String symbol) {
        log.debug("Deleting index with symbol: {}", symbol);
        return indexService.deleteIndex(symbol);
    }
}
