package com.jds.neo4j.reactive.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.model.ExchangeProto.Exchange;
import com.jds.neo4j.reactive.model.TickerProto.Ticker;
import com.jds.neo4j.reactive.repository.TickerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Ticker Service Test")
class TickerServiceTest {

    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private ExchangeService exchangeService;
    private TickerServiceImpl tickerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        tickerService = new TickerServiceImpl(tickerRepository, exchangeService, objectMapper);
    }

    @Test
    void testGetAllTickers() {
        when(tickerRepository.findAll()).thenReturn(Flux.just(new TickerNode()));
        tickerService.getAllTickers().as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        verify(tickerRepository).findAll();
    }

    @Test
    void testGetTickerById() {
        when(tickerRepository.findById(1L)).thenReturn(Mono.just(new TickerNode()));
        tickerService.getTickerById(1L).as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        verify(tickerRepository).findById(1L);
    }

    @Test
    void testCreateTickerWithJson() throws Exception {
        Ticker ticker = Ticker.newBuilder()
                .setSymbol("ABC")
                .setName("ABC Inc.")
                .setExchange(Exchange.newBuilder()
                        .setCode("NYSE")
                        .setName("New York Stock Exchange")
                        .setCountry("USA")
                        .build())
                .setTimestamp(123456789L)
                .build();
        String tickerJson = JsonFormat.printer().print(ticker);

        when(tickerRepository.save(any())).thenReturn(Mono.just(new TickerNode()));
        when(exchangeService.getExchangeNodeFromProto(any())).thenReturn(new ExchangeNode());

        StepVerifier.create(tickerService.createTicker(tickerJson))
                .expectNextCount(1)
                .verifyComplete();

        verify(exchangeService).getExchangeNodeFromProto(any());
        verify(tickerRepository).save(any());
    }

    @Test
    void testCreateTickerWithProto() {
        Ticker ticker = Ticker.newBuilder()
                .setSymbol("ABC")
                .setName("ABC Inc.")
                .setExchange(Exchange.newBuilder()
                        .setCode("NYSE")
                        .setName("New York Stock Exchange")
                        .setCountry("USA")
                        .build())
                .setTimestamp(123456789L)
                .build();

        when(exchangeService.getExchangeNodeFromProto(any())).thenReturn(new ExchangeNode());
        when(tickerRepository.save(any())).thenReturn(Mono.just(new TickerNode()));

        tickerService.createTicker(ticker).as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        verify(exchangeService).getExchangeNodeFromProto(any());
        verify(tickerRepository).save(any());
    }

    @Test
    public void testUpdateTickerWithJson() throws InvalidProtocolBufferException, JsonProcessingException {
        // Arrange
        TickerNode existingTicker = new TickerNode("AAPL", "Apple Inc.", new ExchangeNode("NASDAQ", "NASDAQ Stock Exchange", "USA"), 1646000000L);
        when(tickerRepository.findById(1L)).thenReturn(Mono.just(existingTicker));

        Exchange exchange = Exchange.newBuilder().setCode("NYSE").setName("New York Stock Exchange").setCountry("USA").build();
        String updatedTickerJson = "{\"symbol\":\"AAPL\",\"name\":\"Apple Inc.\",\"exchange\":{\"code\":\"NYSE\",\"name\":\"New York Stock Exchange\",\"country\":\"USA\"},\"timestamp\":1646100000}";

        when(tickerRepository.save(any())).thenReturn(Mono.just(new TickerNode("AAPL", "Apple Inc.", new ExchangeNode("NYSE", "New York Stock Exchange", "USA"), 1646100000L)));

        // Act
        Mono<TickerNode> result = tickerService.updateTicker(1L, updatedTickerJson);

        // Assert
        StepVerifier.create(result)
                .assertNext(updatedTicker -> {
                    assertThat(updatedTicker.getSymbol()).isEqualTo("AAPL");
                    assertThat(updatedTicker.getName()).isEqualTo("Apple Inc.");
                    assertThat(updatedTicker.getExchange().getCode()).isEqualTo("NYSE");
                    assertThat(updatedTicker.getExchange().getName()).isEqualTo("New York Stock Exchange");
                    assertThat(updatedTicker.getExchange().getCountry()).isEqualTo("USA");
                    assertThat(updatedTicker.getTimestamp()).isEqualTo(1646100000L);
                })
                .verifyComplete();
    }

    @Test
    public void testDeleteTicker() {
        // Given
        Long tickerId = 1L;

        // When
        when(tickerRepository.deleteById(tickerId)).thenReturn(Mono.empty());
        Mono<Void> result = tickerService.deleteTicker(tickerId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(tickerRepository, times(1)).deleteById(tickerId);
    }
}
