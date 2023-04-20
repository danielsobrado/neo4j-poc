package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Price")
public class PriceNode {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Relationship(type = "HAS_TICKER", direction = Relationship.Direction.INCOMING)
    @NonNull
    private TickerNode ticker;

    private Double open;

    private Double high;

    private Double low;

    @NonNull
    private Double close;

    private Double volume;

    @Relationship(type = "WITH_CURRENCY", direction = Relationship.Direction.INCOMING)
    private CurrencyNode currency;

    @Relationship(type = "LISTED_ON", direction = Relationship.Direction.INCOMING)
    private ExchangeNode exchange;
    @NonNull
    private Long timestamp;

    public PriceNode(TickerNode tickerNode, double open, double high, double low, double close, double volume, CurrencyNode currency, ExchangeNode exchangeNode, long currentTimeMillis) {
        this.ticker = tickerNode;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.currency = currency;
        this.exchange = exchangeNode;
        this.timestamp = currentTimeMillis;
    }
}
