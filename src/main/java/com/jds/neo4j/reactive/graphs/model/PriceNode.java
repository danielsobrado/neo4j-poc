package com.jds.neo4j.reactive.graphs.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

    private Double open;

    private Double high;

    private Double low;

    @NonNull
    private Double close;

    private Double volume;

    private String name = "Price";

    @NonNull
    private Long timestamp;

    @Relationship(type = "PRICE_FOR", direction = Relationship.Direction.INCOMING)
    private TickerNode ticker;

    public PriceNode(TickerNode ticker, double open, double high, double low, double close, double volume, long currentTimeMillis) {
        this.ticker = ticker;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.timestamp = currentTimeMillis;
        this.name = generateName(currentTimeMillis);
    }

    public PriceNode(TickerNode ticker, double close, long currentTimeMillis) {
        this.ticker = ticker;
        this.close = close;
        this.timestamp = currentTimeMillis;
        this.name = generateName(currentTimeMillis);
    }

    private String generateName(long currentTimeMillis) {
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy");
        return "Price " + date.format(formatter);
    }
}
