package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Trader")
public class TraderNode {

    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String name;
    private Double cash;
    private Set<PortfolioNode> portfolios;
}
