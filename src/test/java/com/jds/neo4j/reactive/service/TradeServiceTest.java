package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.ExchangeProto;
import com.jds.neo4j.reactive.model.TickerProto;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.jds.neo4j.reactive.model.TradeProto.Side;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Trade Service Test")
@RunWith(SpringRunner.class)
public class TradeServiceTest {

    @Mock
    ExchangeService exchangeService;

    @Mock
    private TradeRepository tradeRepository;
    @Mock
    private TickerService tickerService;
    @InjectMocks
    private TradeServiceImpl tradeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTrades() {
        TickerNode tickerNode = new TickerNode("AAPL");
        ExchangeNode exchangeNode = new ExchangeNode("NASDAQ", "NASDAQ Stock Exchange", "USA");
        TradeNode tradeNode = new TradeNode(tickerNode, 100.0, 10L, Side.BUY, exchangeNode, System.currentTimeMillis());

        // mock repository to return Flux of TradeNode
        when(tradeRepository.findAll()).thenReturn(Flux.just(tradeNode));

        // call the method under test
        Flux<TradeNode> result = tradeService.getAllTrades();

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result contains at least one TradeNode
        assertTrue(Objects.requireNonNull(result.collectList().block()).size() > 0);
    }

    @Test
    public void testGetTradeById() {
        Long id = 1L;

        TickerNode tickerNode = new TickerNode("AAPL");
        ExchangeNode exchangeNode = new ExchangeNode("NASDAQ", "NASDAQ Stock Exchange", "USA");
        TradeNode tradeNode = new TradeNode(tickerNode, 100.0, 10L, Side.BUY, exchangeNode, System.currentTimeMillis());
        tradeNode.setId(id);

        // mock repository to return Mono of TradeNode
        when(tradeRepository.findById(id)).thenReturn(Mono.just(tradeNode));

        // call the method under test
        Mono<TradeNode> result = tradeService.getTradeById(id);

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result contains the expected TradeNode
        assertEquals(id, Objects.requireNonNull(result.block()).getId());
    }

    @Test
    public void testUpdateTrade() {
        Long id = 1L;

        TickerNode tickerNode = new TickerNode("AAPL");
        ExchangeNode exchangeNode = new ExchangeNode("NASDAQ", "NASDAQ Stock Exchange", "USA");
        TradeNode tradeNode = new TradeNode(tickerNode, 100.0, 10L, Side.BUY, exchangeNode, System.currentTimeMillis());
        tradeNode.setId(id);

        // mock repository to return Mono of TradeNode
        when(tradeRepository.findById(id)).thenReturn(Mono.just(tradeNode));
        when(tradeRepository.save(any(TradeNode.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // call the method under test
        Mono<TradeNode> result = tradeService.updateTrade(id, new TradeNode(tickerNode, 110.0, 10L, Side.BUY, exchangeNode, System.currentTimeMillis()));

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result contains the expected TradeNode
        assertEquals(id, Objects.requireNonNull(result.block()).getId());
        assertEquals(110.0, result.block().getPrice(), 0.0);
    }

    private TradeNode createTradeNode(String symbol, Double price, Long quantity, TradeProto.Side side) {
        TickerProto.Ticker ticker = TickerProto.Ticker.newBuilder()
                .setSymbol(symbol)
                .setName("Apple Inc.")
                .setExchange(ExchangeProto.Exchange.newBuilder().setCode("NASDAQ").build())
                .setTimestamp(System.currentTimeMillis())
                .build();

        // Mock the TickerService to return the created TickerProto.Ticker
        when(tickerService.convertToProto(any(TickerNode.class))).thenReturn(ticker);

        TickerNode tickerNode = new TickerNode();
        tickerNode.setSymbol(symbol);
        tickerNode.setName("Apple Inc.");
        tickerNode.setExchange(new ExchangeNode("NASDAQ", "NASDAQ Stock Exchange", "USA"));
        tickerNode.setTimestamp(System.currentTimeMillis());

        TradeNode tradeNode = new TradeNode();
        tradeNode.setTicker(tickerNode);
        tradeNode.setPrice(price);
        tradeNode.setQuantity(quantity);
        tradeNode.setSide(side);
        tradeNode.setTimestamp(System.currentTimeMillis());
        return tradeNode;
    }
}


