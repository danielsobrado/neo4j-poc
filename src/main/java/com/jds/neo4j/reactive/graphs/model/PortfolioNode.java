package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.annotation.Version;
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
@Node("Portfolio")
public class PortfolioNode {

    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String name;

    @Version
    private Long version;

    private Set<TickerNode> tickers;

}
