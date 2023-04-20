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
@Node("Spinoff")
public class SpinoffNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String id;

    @Relationship(type = "PARENT", direction = Relationship.Direction.OUTGOING)
    @EqualsAndHashCode.Include
    private TickerNode parent_ticker;

    @Relationship(type = "SPINOFF", direction = Relationship.Direction.OUTGOING)
    private TickerNode spinoff_ticker;

    @NonNull
    private Long effective_date;

}
