package com.jds.neo4j.reactive.neo4jreactive.repository;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class TradeRepositoryTest {
    @Container
    private static final Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5.5")
            .withAdminPassword(null); // use no password for testing purposes
    private static ServerControls serverControls;
    private final Driver driver;
    TradeRepository tradeRepository;

    public TradeRepositoryTest() {
        serverControls = TestServerBuilders.newInProcessBuilder()
                .withExtension(
                        "/graphql", "com.graphqlneo4j.graphqlneo4j.GraphQLExtensionFactory")
                .newServer();

        driver = GraphDatabase.driver(serverControls.boltURI(),
                AuthTokens.basic("neo4j", "neo4j"));
    }

    @Test
    void saveTrade_shouldSaveAndRetrieveTrade() {
        ExchangeNode exchangeNode = new ExchangeNode();
        exchangeNode.setCode("NASDAQ");

        TradeNode tradeNode = new TradeNode(1L, "AAPL", 100.0, 10L, TradeProto.Side.BUY, exchangeNode, System.currentTimeMillis());
        StepVerifier.create(tradeRepository.save(tradeNode))
                .expectNextMatches(savedTrade -> savedTrade.getId() != null)
                .verifyComplete();

        List<TradeNode> trades = tradeRepository.findBySymbol("ABC").collectList().block();

        assertThat(trades).hasSize(1);
        assertThat(trades.get(0)).isEqualTo(tradeNode);
    }
}


