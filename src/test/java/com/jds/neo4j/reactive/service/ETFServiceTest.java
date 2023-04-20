package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.ETFNode;
import com.jds.neo4j.reactive.repository.ETFRepository;
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

@DisplayName("ETF Service Test")
class ETFServiceTest {

    @Mock
    ETFRepository etfRepository;

    ETFService etfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        etfService = new ETFServiceImpl(etfRepository);
    }

    @Test
    void getAllETFs_ReturnsAllETFs() {
        when(etfRepository.findAll()).thenReturn(Flux.just(
                new ETFNode("SPY", "SPDR S&P 500 ETF Trust"),
                new ETFNode("QQQ", "Invesco QQQ Trust")
        ));

        Flux<ETFNode> result = etfService.getAllETFs();

        StepVerifier.create(result)
                .expectNext(new ETFNode("SPY", "SPDR S&P 500 ETF Trust"))
                .expectNext(new ETFNode("QQQ", "Invesco QQQ Trust"))
                .verifyComplete();
    }

    @Test
    void getETFBySymbol_ReturnsETF() {
        String symbol = "SPY";
        when(etfRepository.findById(symbol)).thenReturn(Mono.just(
                new ETFNode("SPY", "SPDR S&P 500 ETF Trust")
        ));

        Mono<ETFNode> result = etfService.getETFBySymbol(symbol);

        StepVerifier.create(result)
                .expectNext(new ETFNode("SPY", "SPDR S&P 500 ETF Trust"))
                .verifyComplete();
    }

    @Test
    void updateETF_ReturnsUpdatedETF() {
        String symbol = "SPY";
        ETFNode updatedETF = new ETFNode("SPY", "SPDR S&P 500 ETF Trust");
        when(etfRepository.findById(symbol)).thenReturn(Mono.just(
                new ETFNode("SPY", "Invesco QQQ Trust")
        ));
        when(etfRepository.save(any(ETFNode.class))).thenReturn(Mono.just(
                updatedETF
        ));

        Mono<ETFNode> result = etfService.updateETF(symbol, updatedETF);

        StepVerifier.create(result)
                .expectNext(updatedETF)
                .verifyComplete();
    }

    @Test
    void deleteETF_DeletesETF() {
        String symbol = "SPY";
        when(etfRepository.deleteById(symbol)).thenReturn(Mono.empty());

        Mono<Void> result = etfService.deleteETF(symbol);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
