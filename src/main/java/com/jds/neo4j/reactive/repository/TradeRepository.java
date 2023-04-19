package com.jds.neo4j.reactive.repository;

import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public interface TradeRepository extends ReactiveNeo4jRepository<TradeNode, Long> {

    @Query("MATCH (t:Trade)-[:HAS_TICKER]->(ticker:Ticker) WHERE ticker.symbol = $symbol RETURN t, ticker")
    Flux<TradeNode> findBySymbol(String symbol);

    @Query("MERGE (t:Ticker {symbol: $tickerSymbol}) " +
            "MERGE (e:ExchangeNode {code: $exchangeCode}) " +
            "MERGE (t)-[:LISTED_ON]->(e) " +
            "RETURN t, properties(t), e, properties(e)")
    Mono<TickerNode> findOrCreateTickerNode(String tickerSymbol, String exchangeCode);

    @Query("MATCH (tr:Trade)-[:HAS_TICKER]->(t:Ticker) " +
            "WHERE tr.timestamp >= $startTime AND tr.timestamp <= $endTime " +
            "RETURN tr, properties(tr), t, properties(t)")
    Flux<Map<String, Object>> findTradesWithinTimeRange(long startTime, long endTime);

}

