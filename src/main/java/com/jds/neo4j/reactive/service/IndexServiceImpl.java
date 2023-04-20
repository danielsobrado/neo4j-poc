package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.IndexNode;
import com.jds.neo4j.reactive.repository.IndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {
    private final IndexRepository indexRepository;

    @Override
    public Flux<IndexNode> getAllIndexes() {
        log.debug("Getting all indexes");
        return indexRepository.findAll();
    }

    @Override
    public Mono<IndexNode> getIndexBySymbol(String symbol) {
        log.debug("Getting index by symbol: {}", symbol);
        return indexRepository.findById(symbol);
    }

    @Override
    public Mono<IndexNode> createIndex(IndexNode indexNode) {
        log.debug("Creating index: {}", indexNode);
        return indexRepository.saveWithRetry(indexNode);
    }

    @Override
    public Mono<IndexNode> updateIndex(String symbol, IndexNode indexNode) {
        log.debug("Updating index with symbol: {}, data: {}", symbol, indexNode);
        return indexRepository.findById(symbol)
                .map(existing -> {
                    existing.setName(indexNode.getName());
                    existing.setComponents(indexNode.getComponents());
                    return existing;
                })
                .flatMap(indexRepository::save);
    }

    @Override
    public Mono<Void> deleteIndex(String symbol) {
        log.debug("Deleting index with symbol: {}", symbol);
        return indexRepository.deleteById(symbol);
    }

}
