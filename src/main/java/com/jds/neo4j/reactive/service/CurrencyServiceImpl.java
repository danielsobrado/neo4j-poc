package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.model.CurrencyProto.Currency;
import com.jds.neo4j.reactive.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    public Flux<CurrencyNode> getAllCurrencies() {
        log.debug("Getting all currencies");
        return currencyRepository.findAll();
    }

    @Override
    public Mono<CurrencyNode> getCurrencyByCode(String code) {
        log.debug("Getting currency by code: {}", code);
        return currencyRepository.findById(code);
    }

    @Override
    public Mono<CurrencyNode> createCurrency(CurrencyNode currencyNode) {
        log.debug("Creating currency: {}", currencyNode);
        return currencyRepository.saveWithRetry(currencyNode);
    }

    @Override
    public Mono<CurrencyNode> updateCurrency(String code, CurrencyNode currencyNode) {
        log.debug("Updating currency with code: {}, data: {}", code, currencyNode);
        return currencyRepository.findById(code)
                .map(existing -> {
                    existing.setName(currencyNode.getName());
                    existing.setSymbol(currencyNode.getSymbol());
                    return existing;
                })
                .flatMap(currencyRepository::save);
    }

    @Override
    public Mono<Void> deleteCurrency(String code) {
        log.debug("Deleting currency with code: {}", code);
        return currencyRepository.deleteById(code);
    }

    @Override
    public CurrencyNode convertToNode(Currency currency) {
        return new CurrencyNode(currency.getCode(), currency.getName(), currency.getSymbol());
    }

    @Override
    public Currency convertToProto(CurrencyNode currencyNode) {
        return Currency.newBuilder()
                .setCode(currencyNode.getCode())
                .setName(currencyNode.getName())
                .setSymbol(currencyNode.getSymbol())
                .build();
    }
}
