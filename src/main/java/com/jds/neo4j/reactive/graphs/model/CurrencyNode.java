package com.jds.neo4j.reactive.graphs.model;

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
@Node("Currency")
public class CurrencyNode {

    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private String code;

    private String name;

    private String symbol;

    @Version
    private Long version;

}
