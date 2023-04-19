package com.jds.neo4j.reactive.graphs.model;


import com.jds.neo4j.reactive.model.TickerProto;
import lombok.*;
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
@Node("Ticker")
public class TickerNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String symbol;
    private String name;

    @Relationship(type = "LISTED_ON", direction = Relationship.Direction.OUTGOING)
    @EqualsAndHashCode.Include
    private ExchangeNode exchange;

    @NonNull
    private Long timestamp;

    public TickerNode(TickerProto.Ticker ticker) {
        this.symbol = ticker.getSymbol();
        this.name = ticker.getName();
        this.exchange = new ExchangeNode(ticker.getExchange());
        this.timestamp = ticker.getTimestamp();
    }

    public TickerNode(String aapl) {
        this.symbol = aapl;
    }
}
