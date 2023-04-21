package com.jds.neo4j.reactive.graphs.model;

import com.jds.neo4j.reactive.model.ExchangeProto;
import lombok.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Exchange")
public class ExchangeNode {

    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private String code;

    private String name;

    private String country;

    @Version
    private Long version;

    public ExchangeNode(ExchangeProto.Exchange exchange) {
        this.code = exchange.getCode();
        this.name = exchange.getName();
        this.country = exchange.getCountry();
    }
}
