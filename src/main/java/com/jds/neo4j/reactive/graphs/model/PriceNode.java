package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Price")
public class PriceNode {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    private String symbol;

    private Double open;

    private Double high;

    private Double low;

    @NonNull
    private Double close;

    private Double volume;

    private CurrencyNode currency;

    private ExchangeNode exchange;
    @NonNull
    private Long timestamp;
}
