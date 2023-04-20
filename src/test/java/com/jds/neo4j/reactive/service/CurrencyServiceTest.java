package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Currency Service Test")
class CurrencyServiceTest {

    @Mock
    CurrencyRepository currencyRepository;

    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyServiceImpl(currencyRepository);
    }

    @Test
    void getAllCurrencies_ReturnsAllCurrencies() {
        when(currencyRepository.findAll()).thenReturn(Flux.just(
                new CurrencyNode("USD", "US Dollar", "$"),
                new CurrencyNode("EUR", "Euro", "€")
        ));

        Flux<CurrencyNode> result = currencyService.getAllCurrencies();

        StepVerifier.create(result)
                .expectNext(new CurrencyNode("USD", "US Dollar", "$"))
                .expectNext(new CurrencyNode("EUR", "Euro", "€"))
                .verifyComplete();
    }

    @Test
    void getCurrencyByCode_ReturnsCurrency() {
        String code = "USD";
        when(currencyRepository.findById(code)).thenReturn(Mono.just(
                new CurrencyNode("USD", "US Dollar", "$")
        ));

        Mono<CurrencyNode> result = currencyService.getCurrencyByCode(code);

        StepVerifier.create(result)
                .expectNext(new CurrencyNode("USD", "US Dollar", "$"))
                .verifyComplete();
    }

    @Test
    void updateCurrency_ReturnsUpdatedCurrency() {
        String code = "USD";
        CurrencyNode updatedCurrency = new CurrencyNode("USD", "US Dollar", "$");
        when(currencyRepository.findById(code)).thenReturn(Mono.just(
                new CurrencyNode("USD", "Euro", "€")
        ));
        when(currencyRepository.save(any(CurrencyNode.class))).thenReturn(Mono.just(
                updatedCurrency
        ));

        Mono<CurrencyNode> result = currencyService.updateCurrency(code, updatedCurrency);

        StepVerifier.create(result)
                .expectNext(updatedCurrency)
                .verifyComplete();
    }

    @Test
    void deleteCurrency_DeletesCurrency() {
        String code = "USD";
        when(currencyRepository.deleteById(code)).thenReturn(Mono.empty());

        Mono<Void> result = currencyService.deleteCurrency(code);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
