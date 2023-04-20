package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.SpinoffNode;
import com.jds.neo4j.reactive.service.SpinoffServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spinoffs")
@Slf4j
@RequiredArgsConstructor
public class SpinoffController {
    private final SpinoffServiceImpl spinoffService;

    @GetMapping
    public Flux<SpinoffNode> getAllSpinoffs() {
        log.debug("Getting all spinoffs");
        return spinoffService.getAllSpinoffs();
    }

    @GetMapping("/{symbol}")
    public Mono<SpinoffNode> getSpinoffBySymbol(@PathVariable("symbol") String symbol) {
        log.debug("Getting spinoff by symbol: {}", symbol);
        return spinoffService.getSpinoffById(symbol);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SpinoffNode> createSpinoff(@RequestBody SpinoffNode spinoffNode) {
        log.debug("Creating spinoff: {}", spinoffNode);
        return spinoffService.createSpinoff(spinoffNode);
    }

    @PutMapping("/{symbol}")
    public Mono<SpinoffNode> updateSpinoff(@PathVariable("symbol") String symbol, @RequestBody SpinoffNode spinoffNode) {
        log.debug("Updating spinoff with symbol: {}, data: {}", symbol, spinoffNode);
        return spinoffService.updateSpinoff(symbol, spinoffNode);
    }

    @DeleteMapping("/{symbol}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteSpinoff(@PathVariable("symbol") String symbol) {
        log.debug("Deleting spinoff with symbol: {}", symbol);
        return spinoffService.deleteSpinoff(symbol);
    }
}
