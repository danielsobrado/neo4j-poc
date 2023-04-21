package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Exchange")
public class ExchangeNode {

    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private String code;

    @NonNull
    private String name;

    @NonNull
    private String country;

    @NonNull
    private CurrencyNode currency;

    public ExchangeNode(String exchangeCode, String exchangeName, String exchangeCountry) {
        this.code = exchangeCode;
        this.name = exchangeName;
        this.country = exchangeCountry;
    }
}
