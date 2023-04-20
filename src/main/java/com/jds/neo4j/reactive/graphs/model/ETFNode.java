package com.jds.neo4j.reactive.graphs.model;

import com.jds.neo4j.reactive.model.ETFProto;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("ETF")
public class ETFNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String symbol;
    private String name;

    @Relationship(type = "COMPONENT_OF", direction = Relationship.Direction.OUTGOING)
    private List<ETFComponentNode> components;

    public ETFNode(ETFProto.ETF etf) {
        this.symbol = etf.getSymbol();
        this.name = etf.getName();
    }

    public ETFNode(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }
}
