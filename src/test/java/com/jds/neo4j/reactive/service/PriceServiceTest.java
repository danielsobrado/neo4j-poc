package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.PriceNode;
import com.jds.neo4j.reactive.graphs.model.TickerNode;
import com.jds.neo4j.reactive.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Price Service Test")
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    private PriceService priceService;

    private PriceNode testPrice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create Currency
        CurrencyNode currencyNode = new CurrencyNode();
        currencyNode.setSymbol("USD");
        currencyNode.setName("US Dollar");
        currencyNode.setSymbol("$");

        // Create Exchange
        ExchangeNode exchangeNode = new ExchangeNode();
        exchangeNode.setName("Binance");
        exchangeNode.setCode("BINANCE");
        exchangeNode.setCountry("Singapore");


        priceService = new PriceServiceImpl(priceRepository);

        TickerNode tickerNode = new TickerNode();
        tickerNode.setSymbol("BTC");

        testPrice = new PriceNode();
        testPrice.setTicker(tickerNode);
        testPrice.setOpen(10000.0);
        testPrice.setHigh(11000.0);
        testPrice.setLow(9000.0);
        testPrice.setClose(10500.0);
        testPrice.setVolume(100.0);
        testPrice.setTimestamp(System.currentTimeMillis());
    }

    @Test
    void testGetAllPrices() {
        when(priceRepository.findAll()).thenReturn(Flux.just(testPrice));

        StepVerifier.create(priceService.getAllPrices())
                .expectNext(testPrice)
                .verifyComplete();
    }

    @Test
    void testGetPriceById() {
        when(priceRepository.findById(anyLong())).thenReturn(Mono.just(testPrice));

        StepVerifier.create(priceService.getPriceById(1L))
                .expectNext(testPrice)
                .verifyComplete();
    }

    @Test
    void testUpdatePrice() {

        TickerNode tickerNode = new TickerNode();
        tickerNode.setSymbol("ETH");

        PriceNode updatedPrice = new PriceNode();
        updatedPrice.setTicker(tickerNode);
        updatedPrice.setClose(1000.0);
        updatedPrice.setTimestamp(System.currentTimeMillis());

        when(priceRepository.findById((Long) any())).thenReturn(Mono.just(testPrice));
        when(priceRepository.save(any())).thenReturn(Mono.just(updatedPrice));

        StepVerifier.create(priceService.updatePrice(1L, updatedPrice))
                .expectNextMatches(price -> price.equals(updatedPrice))
                .verifyComplete();
    }

    @Test
    void testDeletePrice() {
        when(priceRepository.deleteById((Long) any())).thenReturn(Mono.empty());

        StepVerifier.create(priceService.deletePrice(1L))
                .verifyComplete();

        verify(priceRepository, times(1)).deleteById((Long) any());
    }

}
