package com.jds.neo4j.reactive.service;

import com.jds.neo4j.reactive.graphs.model.IndexNode;
import com.jds.neo4j.reactive.repository.IndexRepository;
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

@DisplayName("Index Service Test")
class IndexServiceTest {

    @Mock
    IndexRepository indexRepository;

    IndexService indexService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        indexService = new IndexServiceImpl(indexRepository);
    }

    @Test
    void getAllIndexes_ReturnsAllIndexes() {
        when(indexRepository.findAll()).thenReturn(Flux.just(
                new IndexNode("SPX", "S&P 500 Index"),
                new IndexNode("NDX", "Nasdaq 100 Index")
        ));

        Flux<IndexNode> result = indexService.getAllIndexes();

        StepVerifier.create(result)
                .expectNext(new IndexNode("SPX", "S&P 500 Index"))
                .expectNext(new IndexNode("NDX", "Nasdaq 100 Index"))
                .verifyComplete();
    }

    @Test
    void getIndexBySymbol_ReturnsIndex() {
        String symbol = "SPX";
        when(indexRepository.findById(symbol)).thenReturn(Mono.just(
                new IndexNode("SPX", "S&P 500 Index")
        ));

        Mono<IndexNode> result = indexService.getIndexBySymbol(symbol);

        StepVerifier.create(result)
                .expectNext(new IndexNode("SPX", "S&P 500 Index"))
                .verifyComplete();
    }

    @Test
    void updateIndex_ReturnsUpdatedIndex() {
        String symbol = "SPX";
        IndexNode updatedIndex = new IndexNode("SPX", "S&P 500 Index");
        when(indexRepository.findById(symbol)).thenReturn(Mono.just(
                new IndexNode("SPX", "Nasdaq 100 Index")
        ));
        when(indexRepository.save(any(IndexNode.class))).thenReturn(Mono.just(
                updatedIndex
        ));

        Mono<IndexNode> result = indexService.updateIndex(symbol, updatedIndex);

        StepVerifier.create(result)
                .expectNext(updatedIndex)
                .verifyComplete();
    }

    @Test
    void deleteIndex_DeletesIndex() {
        String symbol = "SPX";
        when(indexRepository.deleteById(symbol)).thenReturn(Mono.empty());

        Mono<Void> result = indexService.deleteIndex(symbol);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
