package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Spinoff")
public class SpinoffNode {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    private String id;

    @Relationship(type = "PARENT", direction = Relationship.Direction.OUTGOING)
    @EqualsAndHashCode.Include
    private TickerNode parentTicker;

    @Relationship(type = "SPINOFF", direction = Relationship.Direction.OUTGOING)
    private TickerNode spinoffTicker;

    @NonNull
    private Long effective_date;

    public SpinoffNode(String id, String parentOld, String spinoffOld, LocalDate effectiveDate) {
        this.id = id;
        this.parentTicker = new TickerNode(parentOld);
        this.spinoffTicker = new TickerNode(spinoffOld);
        this.effective_date = effectiveDate.toEpochDay();
    }
}
