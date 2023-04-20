package com.jds.neo4j.reactive.controller;

import com.jds.neo4j.reactive.graphs.model.Spinoff;
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
    public Flux<Spinoff> getAllSpinoffs() {
        log.debug("Getting all spinoffs");
        return spinoffService.getAllSpinoffs();
    }

    @GetMapping("/{id}")
    public Mono<Spinoff> getSpinoffById(@PathVariable("id") Long id) {
        log.debug("Getting spinoff by id: {}", id);
        return spinoffService.getSpinoffById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Spinoff> createSpinoff(@RequestBody Spinoff spinoff) {
        log.debug("Creating spinoff: {}", spinoff);
        return spinoffService.createSpinoff(spinoff);
    }

    @PutMapping("/{id}")
    public Mono<Spinoff> updateSpinoff(@PathVariable("id") Long id, @RequestBody Spinoff spinoff) {
        log.debug("Updating spinoff with id: {}, data: {}", id, spinoff);
        return spinoffService.updateSpinoff(id, spinoff);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteSpinoff(@PathVariable("id") Long id) {
        log.debug("Deleting spinoff with id: {}", id);
        return spinoffService.deleteSpinoff(id);
    }
}
