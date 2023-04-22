package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.annotation.Version;
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
@Node("Portfolio")
public class PortfolioNode {

    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String name;

    @Version
    private Long version;

    @Relationship(type = "HOLDS")
    private List<TickerNode> holdings = new ArrayList<>();

    public void addTicker(TickerNode ticker, Long quantity) {
        holdings.add(ticker);
        ticker.addHoldingPortfolio(this);
    }

}

