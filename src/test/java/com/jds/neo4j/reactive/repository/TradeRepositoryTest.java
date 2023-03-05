package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.TradeProto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.Neo4jContainer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Trade Repository IT Test")
@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@ContextConfiguration(initializers = TradeRepositoryTest.TestContainerInitializer.class)
@Slf4j
class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    void testCreateTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        Flux<TradeNode> trades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(trades)
                .expectNextMatches(trade -> trade.getSymbol().equals("AAPL"))
                .expectComplete()
                .verify();

        tradeRepository.delete(tradeNode).block();
    }

    @Test
    void testUpdateTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        Flux<TradeNode> trades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(trades)
                .expectNextMatches(trade -> trade.getSymbol().equals("AAPL"))
                .expectComplete()
                .verify();

        tradeNode.setQuantity(20L);
        tradeRepository.save(tradeNode).block();

        Flux<TradeNode> updatedTrades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(updatedTrades)
                .expectNextMatches(trade -> trade.getQuantity().equals(20L))
                .expectComplete()
                .verify();

        tradeRepository.delete(tradeNode).block();
    }

    @Test
    void testDeleteTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        tradeRepository.delete(tradeNode).block();

        Flux<TradeNode> trades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(trades)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    void testFindTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        Mono<TradeNode> foundTrade = tradeRepository.findById(tradeNode.getId());
        StepVerifier.create(foundTrade)
                .expectNextMatches(trade -> trade.getSymbol().equals("AAPL"))
                .expectComplete()
                .verify();

        tradeRepository.delete(tradeNode).block();
    }

    private TradeNode createTradeNode(String symbol, Double price, Long quantity, TradeProto.Side side) {
        ExchangeNode exchangeNode = new ExchangeNode();
        exchangeNode.setCode("NASDAQ");

        TradeNode tradeNode = new TradeNode();
        tradeNode.setSymbol(symbol);
        tradeNode.setPrice(price);
        tradeNode.setQuantity(quantity);
        tradeNode.setSide(side);
        tradeNode.setExchange(exchangeNode);
        tradeNode.setTimestamp(System.currentTimeMillis());
        return tradeNode;
    }

    static class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:4.0").withoutAuthentication();
            neo4jContainer.start();
            configurableApplicationContext
                    .addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> neo4jContainer.stop());
            TestPropertyValues
                    .of(
                            "spring.neo4j.uri=" + neo4jContainer.getBoltUrl(),
                            "spring.neo4j.authentication.username=neo4j",
                            "spring.neo4j.authentication.password=" + neo4jContainer.getAdminPassword()
                    )
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}


