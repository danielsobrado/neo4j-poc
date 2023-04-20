package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.repository.ExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DisplayName("Exchange Service Test")
class ExchangeServiceTest {

    @Mock
    ExchangeRepository exchangeRepository;

    ExchangeService exchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exchangeService = new ExchangeServiceImpl(exchangeRepository);
    }

    @Test
    void getAllExchanges_ReturnsAllExchanges() {
        when(exchangeRepository.findAll()).thenReturn(Flux.just(
                new ExchangeNode("CODE1", "Exchange1", "Country1"),
                new ExchangeNode("CODE2", "Exchange2", "Country2")
        ));

        Flux<ExchangeNode> result = exchangeService.getAllExchanges();

        StepVerifier.create(result)
                .expectNext(new ExchangeNode("CODE1", "Exchange1", "Country1"))
                .expectNext(new ExchangeNode("CODE2", "Exchange2", "Country2"))
                .verifyComplete();
    }

    @Test
    void getExchangeById_ReturnsExchange() {
        String code = "CODE1";
        when(exchangeRepository.findById(code)).thenReturn(Mono.just(
                new ExchangeNode("CODE1", "Exchange1", "Country1")
        ));

        Mono<ExchangeNode> result = exchangeService.getExchangeById(code);

        StepVerifier.create(result)
                .expectNext(new ExchangeNode("CODE1", "Exchange1", "Country1"))
                .verifyComplete();
    }

    @Test
    void createExchange_ReturnsCreatedExchange() {
        ExchangeNode exchangeNode = new ExchangeNode("CODE1", "Exchange1", "Country1");
        when(exchangeRepository.save(any(ExchangeNode.class))).thenReturn(Mono.just(exchangeNode));

        Mono<ExchangeNode> result = exchangeService.createExchange(exchangeNode);

        StepVerifier.create(result)
                .expectNext(exchangeNode)
                .verifyComplete();
    }

    @Test
    void updateExchange_ReturnsUpdatedExchange() {
        String code = "CODE1";
        ExchangeNode updatedExchange = new ExchangeNode("CODE1", "Exchange2", "Country2");
        when(exchangeRepository.findById(code)).thenReturn(Mono.just(
                new ExchangeNode("CODE1", "Exchange1", "Country1")
        ));

        // Use doAnswer() to assert that the save() method is called with the expected ExchangeNode
        doAnswer(invocation -> {
            ExchangeNode argument = invocation.getArgument(0);
            assertEquals(updatedExchange, argument);
            return Mono.just(argument);
        }).when(exchangeRepository).save(any(ExchangeNode.class));

        Mono<ExchangeNode> result = exchangeService.updateExchange(code, updatedExchange);

        StepVerifier.create(result)
                .expectNext(updatedExchange)
                .verifyComplete();
    }


    @Test
    void deleteExchange_DeletesExchange() {
        String code = "CODE1";
        when(exchangeRepository.deleteById(code)).thenReturn(Mono.empty());

        Mono<Void> result = exchangeService.deleteExchange(code);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
