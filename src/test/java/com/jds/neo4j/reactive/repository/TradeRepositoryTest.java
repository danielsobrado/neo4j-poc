package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.*;
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
import reactor.test.StepVerifier;

import java.util.Objects;

@DisplayName("Trade Repository IT Test")
@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@ContextConfiguration(initializers = TradeRepositoryTest.TestContainerInitializer.class)
@Slf4j
class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Test
    void testCreateTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        Flux<TradeNode> trades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(trades).expectNextMatches(trade -> trade.getTicker().getSymbol().equals("AAPL")).expectComplete().verify();

        tradeRepository.delete(tradeNode).block();
    }

    @Test
    void testUpdateTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        Flux<TradeNode> trades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(trades).expectNextMatches(trade -> trade.getTicker().getSymbol().equals("AAPL")).expectComplete().verify();

        tradeNode.setQuantity(20L);
        tradeRepository.save(tradeNode).block();

        Flux<TradeNode> updatedTrades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(updatedTrades).expectNextMatches(trade -> trade.getQuantity().equals(20L)).expectComplete().verify();

        tradeRepository.delete(tradeNode).block();
    }

    @Test
    void testDeleteTrade() {
        TradeNode tradeNode = createTradeNode("AAPL", 100.0, 10L, TradeProto.Side.BUY);
        tradeRepository.save(tradeNode).block();

        tradeRepository.delete(tradeNode).block();

        Flux<TradeNode> trades = tradeRepository.findBySymbol("AAPL");
        StepVerifier.create(trades).expectNextCount(0).expectComplete().verify();
    }

    @Test
    public void testFindTrade() {
        // Define your test data and parameters
        String tickerSymbol = "AAPL";
        String tickerName = "Apple Inc.";
        String exchangeCode = "NASDAQ";
        String exchangeName = "Nasdaq Stock Market";
        String exchangeCountry = "United States";
        double tradePrice = 100.0;
        long tradeQuantity = 10;
        TradeProto.Side tradeSide = TradeProto.Side.BUY;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 60 * 1000; // Add 1 minute to the start time

        // Create ExchangeNode, TickerNode, and TradeNode objects
        ExchangeNode exchangeNode = new ExchangeNode(exchangeCode, exchangeName, exchangeCountry);
        TickerNode tickerNode = new TickerNode(tickerSymbol, tickerName, exchangeNode, startTime);
        TraderNode traderNode = new TraderNode("John Doe");
        TradeNode tradeNode = new TradeNode(tickerNode, tradePrice, tradeQuantity, tradeSide, startTime, traderNode);

        // Save TickerNode and TradeNode objects to the repository
        System.out.println("Saving tickerNode: " + tickerNode);
        tickerRepository.save(tickerNode).as(StepVerifier::create).expectNextCount(1).verifyComplete();

        System.out.println("Saving tradeNode: " + tradeNode);
        tradeRepository.save(tradeNode).as(StepVerifier::create).expectNextCount(1).verifyComplete();


        // Find trades within the specified time range
        StepVerifier.create(tradeRepository.findTradesWithinTimeRange(startTime, endTime).doOnNext(result -> System.out.println("Query result: " + result)).map(result -> {
            TradeNode trade = (TradeNode) result.get("tr");
            TickerNode ticker = (TickerNode) result.get("t");
            trade.setTicker(ticker);
            return trade;
        })).expectNextMatches(foundTrade -> {
            // Use Objects.equals() method to avoid NullPointerException
            return Objects.equals(foundTrade.getTicker().getSymbol(), tickerSymbol)
                    && Objects.equals(foundTrade.getTicker().getName(), tickerName)
                    && Objects.equals(foundTrade.getTicker().getExchange().getCode(), exchangeCode)
                    && Objects.equals(foundTrade.getPrice().getClose(), tradePrice)
                    && foundTrade.getQuantity() == tradeQuantity
                    && foundTrade.getSide() == tradeSide
                    && foundTrade.getTimestamp() >= startTime
                    && foundTrade.getTimestamp() <= endTime;
        }).verifyComplete();
    }

    private TradeNode createTradeNode(String ticker, Double price, Long quantity, TradeProto.Side side) {
        TickerNode tickerNode = tradeRepository.findOrCreateTickerNode(ticker, "NASDAQ").block();

        TradeNode tradeNode = new TradeNode();
        tradeNode.setTicker(tickerNode);
        tradeNode.setPrice(new PriceNode(tickerNode, price.doubleValue(), System.currentTimeMillis()));
        tradeNode.setQuantity(quantity);
        tradeNode.setSide(side);
        tradeNode.setTimestamp(System.currentTimeMillis());
        return tradeNode;
    }

    static class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:5.5").withoutAuthentication();
            neo4jContainer.start();
            configurableApplicationContext.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> neo4jContainer.stop());
            TestPropertyValues.of("spring.neo4j.uri=" + neo4jContainer.getBoltUrl(), "spring.neo4j.authentication.username=neo4j", "spring.neo4j.authentication.password=" + neo4jContainer.getAdminPassword()).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}


