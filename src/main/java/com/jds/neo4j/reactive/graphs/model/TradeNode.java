package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import static com.jds.neo4j.reactive.model.TradeProto.Side;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node
public class TradeNode {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;
    @NonNull
    private String symbol;

    @NonNull
    private Double price;

    @NonNull
    private Long quantity;

    private Side side;


    private ExchangeNode exchange;

    @NonNull
    private Long timestamp;
}
