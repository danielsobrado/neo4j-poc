package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RelationshipProperties
public class ETFComponent {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private TickerNode ticker;

    @NonNull
    private Double weight;

    public ETFComponent(TickerNode tickerNode, double weight) {
        this.ticker = tickerNode;
        this.weight = weight;
    }
}
