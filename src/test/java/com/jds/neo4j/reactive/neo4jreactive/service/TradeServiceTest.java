package com.jds.neo4j.reactive.neo4jreactive.service;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.TradeRepository;
import com.jds.neo4j.reactive.service.TradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TradeServiceTest {

    @Container
    public static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:5.5");
    ExchangeNode exchangeNode;
    @Autowired
    private TradeService tradeService;
    @MockBean
    private TradeRepository tradeRepository;

    @BeforeEach
    public void setup() {
        neo4jContainer.start();
        exchangeNode = new ExchangeNode();
        exchangeNode.setCode("NASDAQ");
    }

    @AfterEach
    public void tearDown() {
        neo4jContainer.stop();
    }

    @Test
    public void testGetAllTrades() {
        TradeNode tradeNode1 = new TradeNode(1L, "AMZN", 100.0, 10L, TradeProto.Side.BUY, exchangeNode, System.currentTimeMillis());
        TradeNode tradeNode2 = new TradeNode(2L, "AAPL", 200.0, 10L, TradeProto.Side.BUY, exchangeNode, System.currentTimeMillis());
        TradeNode tradeNode3 = new TradeNode(3L, "GOOG", 150.0, 10L, TradeProto.Side.BUY, exchangeNode, System.currentTimeMillis());

        when(tradeRepository.findAll()).thenReturn(Flux.just(tradeNode1, tradeNode2, tradeNode3));

        Flux<TradeNode> result = tradeService.getAllTrades();
        StepVerifier.create(result)
                .expectNext(tradeNode1)
                .expectNext(tradeNode2)
                .expectNext(tradeNode3)
                .verifyComplete();
    }

    @Test
    public void testAddTrade() {
        TradeNode tradeNode1 = new TradeNode(1L, "AMZN", 100.0, 10L, TradeProto.Side.BUY, exchangeNode, System.currentTimeMillis());

        when(tradeRepository.save(any(TradeNode.class))).thenReturn(Mono.just(tradeNode1));

        Mono<TradeNode> result = tradeService.createTrade(tradeNode1);
        StepVerifier.create(result)
                .expectNext(tradeNode1)
                .verifyComplete();
    }

}
