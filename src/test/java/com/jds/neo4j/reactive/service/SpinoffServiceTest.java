package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.Spinoff;
import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.repository.SpinoffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Spinoff Service Test")
class SpinoffServiceTest {

    @Mock
    SpinoffRepository spinoffRepository;

    SpinoffService spinoffService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spinoffService = new SpinoffServiceImpl(spinoffRepository);
    }

    @Test
    void getAllSpinoffs_ReturnsAllSpinoffs() {
        TickerNode parentTicker1 = new TickerNode("PARENT", "Parent Company", null, System.currentTimeMillis());
        TickerNode spinoffTicker1 = new TickerNode("SPINOFF", "Spinoff Company", null, System.currentTimeMillis());
        TickerNode parentTicker2 = new TickerNode("PARENT2", "Parent Company 2", null, System.currentTimeMillis());
        TickerNode spinoffTicker2 = new TickerNode("SPINOFF2", "Spinoff Company 2", null, System.currentTimeMillis());

        when(spinoffRepository.findAll()).thenReturn(Flux.just(
                new Spinoff(parentTicker1, spinoffTicker1, 1577836800000L),
                new Spinoff(parentTicker2, spinoffTicker2, 1612137600000L)
        ));

        Flux<Spinoff> result = spinoffService.getAllSpinoffs();

        StepVerifier.create(result)
                .expectNext(new Spinoff(parentTicker1, spinoffTicker1, 1577836800000L))
                .expectNext(new Spinoff(parentTicker2, spinoffTicker2, 1612137600000L))
                .verifyComplete();
    }

    @Test
    void updateSpinoff_ReturnsUpdatedSpinoff() {
        Long id = 1L;
        TickerNode parentTicker = new TickerNode("PARENT", "Parent Company", null, System.currentTimeMillis());
        TickerNode spinoffTicker = new TickerNode("SPINOFF", "Spinoff Company", null, System.currentTimeMillis());
        Spinoff updatedSpinoff = new Spinoff(parentTicker, spinoffTicker, 1577836800000L);

        when(spinoffRepository.findById(id)).thenReturn(Mono.just(
                new Spinoff(new TickerNode("PARENT_OLD", "Parent Company Old", null, System.currentTimeMillis()),
                        new TickerNode("SPINOFF_OLD", "Spinoff Company Old", null, System.currentTimeMillis()),
                        1577836800000L)
        ));
        when(spinoffRepository.save(any(Spinoff.class))).thenReturn(Mono.just(
                updatedSpinoff
        ));

        Mono<Spinoff> result = spinoffService.updateSpinoff(id, updatedSpinoff);
        StepVerifier.create(result)
                .expectNext(updatedSpinoff)
                .verifyComplete();
    }

    @Test
    void deleteSpinoff_DeletesSpinoff() {
        Long id = 1L;
        when(spinoffRepository.deleteById(id)).thenReturn(Mono.empty());
        Mono<Void> result = spinoffService.deleteSpinoff(id);

        StepVerifier.create(result)
                .verifyComplete();
    }
}

