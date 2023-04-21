package com.jds.neo4j.reactive.graphs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class PortfolioHolds {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private TickerNode ticker;

    private Long quantity;

    public PortfolioHolds(TickerNode ticker, Long quantity) {
        this.ticker = ticker;
        this.quantity = quantity;
    }
}
