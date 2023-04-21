package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.annotation.Version;
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
public class IndexComponent {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private TickerNode ticker;

    @NonNull
    private Double weight;

    @Version
    private Long version;

    public IndexComponent(TickerNode tickerNode, double weight) {
        this.ticker = tickerNode;
        this.weight = weight;
    }

}
