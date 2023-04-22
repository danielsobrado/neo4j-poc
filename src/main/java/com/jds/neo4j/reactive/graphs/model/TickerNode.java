package com.jds.neo4j.reactive.graphs.model;


import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Ticker")
public class TickerNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String symbol;

    @NonNull
    private String name;

    @Relationship(type = "LISTED_ON", direction = Relationship.Direction.OUTGOING)
    @EqualsAndHashCode.Include
    @NonNull
    private ExchangeNode exchange;

    @NonNull
    private Long timestamp;

    @Relationship(type = "HOLDS", direction = Relationship.Direction.INCOMING)
    private List<PortfolioNode> heldByPortfolios = new ArrayList<>();

    public TickerNode(String symbol, String name, ExchangeNode exchange) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }

    public void addHoldingPortfolio(PortfolioNode portfolio) {
        heldByPortfolios.add(portfolio);
    }

}
