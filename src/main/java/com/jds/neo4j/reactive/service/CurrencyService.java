package com.jds.neo4j.reactive.service;


import com.jds.neo4j.reactive.graphs.model.CurrencyNode;
import com.jds.neo4j.reactive.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Flux<CurrencyNode> getAllCurrencies() {
        log.debug("Getting all currencies");
        return currencyRepository.findAll();
    }

    public Mono<CurrencyNode> getCurrencyById(Long id) {
        log.debug("Getting currency by id: {}", id);
        return currencyRepository.findById(id);
    }

    public Mono<CurrencyNode> createCurrency(CurrencyNode currency) {
        log.debug("Creating currency: {}", currency);
        return currencyRepository.save(currency);
    }

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

    public Mono<Void> deleteCurrency(Long id) {
        log.debug("Deleting currency with id: {}", id);
        return currencyRepository.deleteById(id);
    }
}




