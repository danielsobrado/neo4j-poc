package com.jds.neo4j.reactive.service;


import com.jds.neo4j.reactive.graphs.model.PriceNode;
import com.jds.neo4j.reactive.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    @Override
    public Flux<PriceNode> getAllPrices() {
        log.debug("Getting all prices");
        return priceRepository.findAll();
    }

    @Override
    public Mono<PriceNode> getPriceById(Long id) {
        log.debug("Getting price by id: {}", id);
        return priceRepository.findById(id);
    }

    @Override
    public Mono<PriceNode> createPrice(PriceNode price) {
        log.debug("Creating price: {}", price);
        return priceRepository.save(price);
    }

    @Override
    public Mono<PriceNode> updatePrice(Long id, PriceNode price) {
        log.debug("Updating price with id: {}, data: {}", id, price);
        return priceRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(price.getSymbol());
                    existing.setOpen(price.getOpen());
                    existing.setHigh(price.getHigh());
                    existing.setLow(price.getLow());
                    existing.setClose(price.getClose());
                    existing.setVolume(price.getVolume());
                    existing.setCurrency(price.getCurrency());
                    existing.setExchange(price.getExchange());
                    existing.setTimestamp(price.getTimestamp());
                    return existing;
                })
                .flatMap(priceRepository::save);
    }

    @Override
    public Mono<Void> deletePrice(Long id) {
        log.debug("Deleting price with id: {}", id);
        return priceRepository.deleteById(id);
    }
}
