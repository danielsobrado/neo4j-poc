package com.jds.neo4j.reactive.graphs.model;


import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

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

}
