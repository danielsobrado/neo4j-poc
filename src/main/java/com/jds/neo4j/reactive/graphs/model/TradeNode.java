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

import static com.jds.neo4j.reactive.model.TradeProto.Side;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Node("Trade")
public class TradeNode {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Relationship(type = "HAS_TICKER", direction = Relationship.Direction.INCOMING)
    private TickerNode ticker;

    @NonNull
    private PriceNode price;

    @NonNull
    private Long quantity;

    @Relationship(type = "BUY", direction = Relationship.Direction.INCOMING)
    private TraderNode buyer;

    @Relationship(type = "SELL", direction = Relationship.Direction.INCOMING)
    private TraderNode seller;

    private Side side;

    @NonNull
    private Long timestamp;

    private String name;

    public TradeNode(TickerNode tickerNode, PriceNode price, long quantity, Side side, long timestamp, TraderNode trader) {
        this.ticker = tickerNode;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.timestamp = timestamp;
        this.name = generateName();

        if (side == Side.BUY) {
            this.buyer = trader;
        } else {
            this.seller = trader;
        }
    }

    public TradeNode(TickerNode tickerNode, double tradePrice, long tradeQuantity, Side tradeSide, long startTime, TraderNode traderNode) {
        this.ticker = tickerNode;
        this.price = new PriceNode(tickerNode, tradePrice, startTime);
        this.quantity = tradeQuantity;
        this.side = tradeSide;
        this.timestamp = startTime;
        this.name = generateName();

        if (tradeSide == Side.BUY) {
            this.buyer = traderNode;
        } else {
            this.seller = traderNode;
        }
    }

    private String generateName() {

        long currentTimeMillis = System.currentTimeMillis();

        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy");
        return "Price " + date.format(formatter);
    }
}
