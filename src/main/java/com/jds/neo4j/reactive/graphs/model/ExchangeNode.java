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
@Node
public class ExchangeNode {
    @Id
    @GeneratedValue
    private Long id;
    @EqualsAndHashCode.Include
    @NonNull
    private String code;
    private String name;
    private String country;

    public ExchangeNode(@NonNull String code, String name, String country) {
        this.code = code;
        this.name = name;
        this.country = country;
    }
}
