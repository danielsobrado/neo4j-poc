package com.jds.neo4j.reactive.graphs.model;


import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Ticker")
public class TickerNode {
    @Id
    @GeneratedValue
    private Long id;
    @EqualsAndHashCode.Include
    @NonNull
    private String symbol;
    private String name;

    @EqualsAndHashCode.Include
    private ExchangeNode exchange;

    @NonNull
    private Long timestamp;

    public TickerNode(@NonNull String symbol, String name, @NonNull ExchangeNode exchange, @NonNull Long timestamp) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.timestamp = timestamp;
    }
}
