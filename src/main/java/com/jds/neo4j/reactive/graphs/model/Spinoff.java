package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RelationshipProperties
public class Spinoff {
    @Id
    @GeneratedValue
    private Long internalId;

    @EqualsAndHashCode.Include
    private TickerNode parentTicker;

    private TickerNode spinoffTicker;

    @NonNull
    private Long effective_date;

    public Spinoff(String parentOld, String spinoffOld, LocalDate effectiveDate) {
        this.parentTicker = new TickerNode(parentOld);
        this.spinoffTicker = new TickerNode(spinoffOld);
        this.effective_date = effectiveDate.toEpochDay();
    }
}
