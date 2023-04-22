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
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Index")
public class IndexNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String symbol;

    private String name;

    @Relationship(type = "COMPONENT_OF", direction = Relationship.Direction.OUTGOING)
    private List<IndexComponent> components = new ArrayList<>();

    public IndexNode(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public void addTicker(TickerNode ticker, Long weight) {
        IndexComponent component = new IndexComponent(ticker, weight);
        components.add(component);
    }

}
