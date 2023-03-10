package com.jds.neo4j.reactive.service;


import com.google.protobuf.InvalidProtocolBufferException;
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
    public Mono<CurrencyNode> getCurrencyById(Long id) {
        log.debug("Getting currency by id: {}", id);

        return currencyRepository.findById(id);
    }

    @Override
    public Mono<CurrencyNode> createCurrency(Currency currency) {
        log.debug("Creating currency: {}", currency);

        CurrencyNode currencyNode = new CurrencyNode(currency.getCode(), currency.getName(), currency.getSymbol());

        return currencyRepository.save(currencyNode);
    }

    @Override
    public Mono<CurrencyNode> createCurrency(String currencyJson) throws InvalidProtocolBufferException {
        log.debug("Creating currency: {}", currencyJson);

        Currency currency = Currency.parseFrom(currencyJson.getBytes());

        CurrencyNode currencyNode = new CurrencyNode(currency.getCode(), currency.getName(), currency.getSymbol());

        return currencyRepository.save(currencyNode);
    }

    @Override
    public Mono<CurrencyNode> updateCurrency(Long id, CurrencyNode currency) {
        log.debug("Updating currency with id: {}, data: {}", id, currency);

        return currencyRepository.findById(id)
                .map(existing -> {
                    existing.setName(currency.getName());
                    existing.setSymbol(currency.getSymbol());
                    return existing;
                })
                .flatMap(currencyRepository::save);
    }

    @Override
    public Mono<CurrencyNode> updateCurrency(Long id, String currencyJson) throws InvalidProtocolBufferException {
        log.debug("Updating currency with id: {}, data: {}", id, currencyJson);

        Currency currency = Currency.parseFrom(currencyJson.getBytes());

        return currencyRepository.findById(id)
                .map(existing -> {
                    existing.setName(currency.getName());
                    existing.setSymbol(currency.getSymbol());
                    return existing;
                })
                .flatMap(currencyRepository::save);
    }

    @Override
    public Mono<Void> deleteCurrency(Long id) {
        log.debug("Deleting currency with id: {}", id);

        return currencyRepository.deleteById(id);
    }

}




