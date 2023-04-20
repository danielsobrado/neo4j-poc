package com.jds.neo4j.reactive.graphs.model;

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
@Node("IndexComponent")
public class IndexComponentNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String id;

    @Relationship(type = "COMPONENT_OF", direction = Relationship.Direction.INCOMING)
    private IndexNode index;

    @Relationship(type = "HAS_TICKER", direction = Relationship.Direction.OUTGOING)
    private TickerNode ticker;

    @NonNull
    private Double weight;
    
}
