package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.Spinoff;
import com.jds.neo4j.reactive.repository.SpinoffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

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
        when(spinoffRepository.findAll()).thenReturn(Flux.just(
                new Spinoff("PARENT", "SPINOFF", LocalDate.of(2021, 1, 1)),
                new Spinoff("PARENT2", "SPINOFF2", LocalDate.of(2021, 2, 1))
        ));

        Flux<Spinoff> result = spinoffService.getAllSpinoffs();

        StepVerifier.create(result)
                .expectNext(new Spinoff("PARENT", "SPINOFF", LocalDate.of(2021, 1, 1)))
                .expectNext(new Spinoff("PARENT2", "SPINOFF2", LocalDate.of(2021, 2, 1)))
                .verifyComplete();
    }
    
    @Test
    void updateSpinoff_ReturnsUpdatedSpinoff() {
        Long id = 1L;
        Spinoff updatedSpinoff = new Spinoff("PARENT", "SPINOFF", LocalDate.of(2021, 1, 1));
        when(spinoffRepository.findById(id)).thenReturn(Mono.just(
                new Spinoff("PARENT_OLD", "SPINOFF_OLD", LocalDate.of(2020, 12, 31))
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
