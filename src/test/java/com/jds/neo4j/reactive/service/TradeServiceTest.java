package com.jds.neo4j.reactive.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.ExchangeProto;
import com.jds.neo4j.reactive.model.ExchangeProto.Exchange;
import com.jds.neo4j.reactive.model.TradeProto.Trade;
import com.jds.neo4j.reactive.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.jds.neo4j.reactive.model.TradeProto.Side;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Trade Service Test")
@RunWith(SpringRunner.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private TradeServiceImpl tradeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(tradeService, "tradeRepository", tradeRepository);
        ReflectionTestUtils.setField(tradeService, "exchangeService", exchangeService);
    }

    @Test
    public void testGetAllTrades() {
        // mock repository to return Flux of TradeNode
        when(tradeRepository.findAll()).thenReturn(Flux.just(new TradeNode()));

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

        TradeNode tradeNode = new TradeNode();
        tradeNode.setId(id);

        // mock repository to return Mono of TradeNode
        when(tradeRepository.findById(id)).thenReturn(Mono.just(tradeNode));

        // call the method under test
        Mono<TradeNode> result = tradeService.getTradeById(id);

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result contains the expected TradeNode
        assertEquals(id, result.block().getId());
    }

    @Test
    public void testCreateTrade() throws InvalidProtocolBufferException {
        String tradeJson = "{\"symbol\": \"AAPL\", \"price\": 100, \"quantity\": 10, \"side\": \"BUY\", \"exchange\": {\"code\": \"NYSE\", \"name\": \"New York Stock Exchange\", \"country\": \"USA\"}, \"timestamp\": 1646534345}";

        ExchangeNode exchangeNode = new ExchangeNode();
        exchangeNode.setCode("NYSE");
        exchangeNode.setName("New York Stock Exchange");
        exchangeNode.setCountry("USA");

        // mock exchangeService to return ExchangeNode
        when(exchangeService.createExchangeNode(any(ExchangeProto.Exchange.class))).thenReturn(exchangeNode);

        Long id = 1L;

        TradeNode tradeNode = new TradeNode();
        tradeNode.setId(id);

        // mock repository to return Mono of TradeNode
        when(tradeRepository.save(any(TradeNode.class))).thenReturn(Mono.just(tradeNode));

        // call the method under test
        Mono<TradeNode> result = tradeService.createTrade(tradeJson);

        // verify that the result is not null
        assertNotNull(result);

        // verify that the result contains the expected TradeNode
        assertTrue(result.block().getId() > 0);
    }

    @Test
    void testCreateTradeFromProto() throws InvalidProtocolBufferException {
        // Mock ExchangeService
        ExchangeService exchangeService = mock(ExchangeService.class);
        ExchangeNode exchangeNode = new ExchangeNode("NYSE", "New York Stock Exchange", "USA");
        when(exchangeService.createExchangeNode(any()))
                .thenReturn(exchangeNode);


        // Create a new Trade message
        Trade tradeProto = Trade.newBuilder()
                .setSymbol("AAPL")
                .setPrice(150.0)
                .setQuantity(100)
                .setSide(Side.BUY)
                .setExchange(Exchange.newBuilder()
                        .setCode("NYSE")
                        .setName("New York Stock Exchange")
                        .setCountry("USA")
                        .build())
                .setTimestamp(System.currentTimeMillis())
                .build();

        // Mock TradeRepository
        TradeRepository tradeRepository = mock(TradeRepository.class);
        TradeNode savedTradeNode = new TradeNode();
        when(tradeRepository.save(any(TradeNode.class)))
                .thenReturn(Mono.just(savedTradeNode));

        // Create a new TradeServiceImpl instance
        TradeServiceImpl tradeService = new TradeServiceImpl(tradeRepository, exchangeService);

        // Call the createTradeFromProto method with the Trade message
        Mono<TradeNode> result = tradeService.createTrade(tradeProto);

        // Verify that the trade was saved to the repository
        StepVerifier.create(result)
                .assertNext(tradeNode -> {
                    assertEquals(savedTradeNode, tradeNode);
                })
                .verifyComplete();
    }


    @Test
    public void testUpdateTrade() throws InvalidProtocolBufferException {
        // Mock the TradeRepository
        TradeRepository tradeRepository = mock(TradeRepository.class);
        when(tradeRepository.save(any(TradeNode.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(tradeRepository.findById(anyLong())).thenAnswer(invocation -> {
            TradeNode tradeNode = new TradeNode();
            tradeNode.setId(1L);
            tradeNode.setSymbol("AAPL");
            tradeNode.setPrice(150.0);
            tradeNode.setQuantity(100L);
            tradeNode.setSide(Side.BUY);
            ExchangeNode exchangeNode = new ExchangeNode();
            exchangeNode.setCode("NYSE");
            exchangeNode.setName("New York Stock Exchange");
            exchangeNode.setCountry("USA");
            tradeNode.setExchange(exchangeNode);
            tradeNode.setTimestamp(System.currentTimeMillis());
            return Mono.just(tradeNode);
        });
        when(tradeRepository.delete(any(TradeNode.class))).thenReturn(Mono.empty());

        // Create a new Trade message
        Trade trade = Trade.newBuilder()
                .setSymbol("AAPL")
                .setPrice(150.0)
                .setQuantity(100)
                .setSide(Side.BUY)
                .setExchange(Exchange.newBuilder()
                        .setCode("NYSE")
                        .setName("New York Stock Exchange")
                        .setCountry("USA"))
                .setTimestamp(System.currentTimeMillis())
                .build();

        // Create the tradeService using the mocked TradeRepository
        TradeService tradeService = new TradeServiceImpl(tradeRepository, exchangeService);

        // Call the createTrade method with the new Trade message
        Mono<TradeNode> createdTrade = tradeService.createTrade(trade);

        // Retrieve the TradeNode from the database
        TradeNode tradeNode = createdTrade.block();

        // Update the TradeNode
        tradeNode.setPrice(160.0);

        // Call the updateTrade method with the updated TradeNode
        Mono<TradeNode> updatedTrade = tradeService.updateTrade(1L, tradeNode);

        // Retrieve the updated TradeNode from the database
        TradeNode updatedTradeNode = updatedTrade.block();

        // Check that the TradeNode was updated correctly
        assertNotNull(updatedTradeNode);
        assertEquals(tradeNode.getSymbol(), updatedTradeNode.getSymbol());
        assertEquals(tradeNode.getPrice(), updatedTradeNode.getPrice());
        assertEquals(tradeNode.getQuantity(), updatedTradeNode.getQuantity());
        assertEquals(tradeNode.getSide(), updatedTradeNode.getSide());
        
    }

}

