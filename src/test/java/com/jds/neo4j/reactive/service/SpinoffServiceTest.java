package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.SpinoffNode;
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
                new SpinoffNode("1", "PARENT", "SPINOFF", LocalDate.of(2021, 1, 1)),
                new SpinoffNode("2", "PARENT2", "SPINOFF2", LocalDate.of(2021, 2, 1))
        ));

        Flux<SpinoffNode> result = spinoffService.getAllSpinoffs();

        StepVerifier.create(result)
                .expectNext(new SpinoffNode("1", "PARENT", "SPINOFF", LocalDate.of(2021, 1, 1)))
                .expectNext(new SpinoffNode("2", "PARENT2", "SPINOFF2", LocalDate.of(2021, 2, 1)))
                .verifyComplete();
    }

    @Test
    void getSpinoffById_ReturnsSpinoff() {
        String id = "1";
        when(spinoffRepository.findById(id)).thenReturn(Mono.just(
                new SpinoffNode("1", "PARENT", "SPINOFF", LocalDate.of(2021, 1, 1))
        ));

        Mono<SpinoffNode> result = spinoffService.getSpinoffById(id);

        StepVerifier.create(result)
                .expectNext(new SpinoffNode("1", "PARENT", "SPINOFF", LocalDate.of(2021, 1, 1)))
                .verifyComplete();
    }

    @Test
    void createSpinoff_ReturnsCreatedSpinoff() {
        SpinoffNode spinoffNode = new SpinoffNode("1", "PARENT", "SPINOFF", LocalDate.of(2021, 1, 1));
        when(spinoffRepository.save(any(SpinoffNode.class))).thenReturn(Mono.just(spinoffNode));

        Mono<SpinoffNode> result = spinoffService.createSpinoff(spinoffNode);

        StepVerifier.create(result)
                .expectNext(spinoffNode)
                .verifyComplete();
    }

    @Test
    void updateSpinoff_ReturnsUpdatedSpinoff() {
        String id = "1";
        SpinoffNode updatedSpinoff = new SpinoffNode("1", "PARENT", "SPINOFF", LocalDate.of(2021, 1, 1));
        when(spinoffRepository.findById(id)).thenReturn(Mono.just(
                new SpinoffNode("1", "PARENT_OLD", "SPINOFF_OLD", LocalDate.of(2020, 12, 31))
        ));
        when(spinoffRepository.save(any(SpinoffNode.class))).thenReturn(Mono.just(
                updatedSpinoff
        ));

        Mono<SpinoffNode> result = spinoffService.updateSpinoff(id, updatedSpinoff);
        StepVerifier.create(result)
                .expectNext(updatedSpinoff)
                .verifyComplete();
    }

    @Test
    void deleteSpinoff_DeletesSpinoff() {
        String id = "1";
        when(spinoffRepository.deleteById(id)).thenReturn(Mono.empty());

        Mono<Void> result = spinoffService.deleteSpinoff(id);

        StepVerifier.create(result)
                .verifyComplete();
    }
}

