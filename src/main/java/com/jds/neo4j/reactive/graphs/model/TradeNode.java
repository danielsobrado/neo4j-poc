package com.jds.neo4j.reactive.graphs.model;

import com.jds.neo4j.reactive.model.TickerProto;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import static com.jds.neo4j.reactive.model.TradeProto.Side;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Trade")
public class TradeNode {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Relationship(type = "HAS_TICKER", direction = Relationship.Direction.INCOMING)
    private TickerNode ticker;

    @NonNull
    private Double price;

    @NonNull
    private Long quantity;

    private Side side;

    @NonNull
    private Long timestamp;

    public TradeNode(TickerProto.Ticker ticker, double price, long quantity, Side side, long timestamp) {
        this.ticker = new TickerNode(ticker);
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.timestamp = timestamp;
    }

    public TradeNode(TickerNode tickerNode, double v, long l, Side buy, ExchangeNode exchangeNode, long timestamp) {
        this.ticker = tickerNode;
        this.price = v;
        this.quantity = l;
        this.side = buy;
        this.timestamp = timestamp;
    }
}
