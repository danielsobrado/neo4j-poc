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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class TickerServiceImpl implements TickerService {

    @NonNull
    private final TickerRepository tickerRepository;

    @NonNull
    private final ExchangeService exchangeService;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<TickerNode> getAllTickers() {
        log.debug("Getting all tickers");

        return tickerRepository.findAll();
    }

    @Override
    public Mono<TickerNode> getTickerById(String id) {
        log.debug("Getting ticker by id: {}", id);

        return tickerRepository.findById(id);
    }

    @Override
    public Mono<TickerNode> createTicker(String tickerJson) throws InvalidProtocolBufferException {
        log.debug("Creating ticker from JSON: {}", tickerJson);

        Ticker.Builder tickerBuilder = Ticker.newBuilder();

        // Parse the JSON string into a Ticker message
        JsonFormat.parser().ignoringUnknownFields().merge(tickerJson, tickerBuilder);

        // Parse the Exchange field from the Ticker message
        Exchange exchange = tickerBuilder.getExchange();

        // Create a new ExchangeNode from the Exchange message
        ExchangeNode exchangeNode = exchangeService.getExchangeNodeFromProto(exchange);

        return tickerRepository.save(new TickerNode(tickerBuilder.getSymbol(), tickerBuilder.getName(), exchangeNode, tickerBuilder.getTimestamp()));
    }

    @Override
    public Mono<TickerNode> updateTicker(String id, String tickerJson) throws InvalidProtocolBufferException, JsonProcessingException {
        log.debug("Updating ticker with id: {} from JSON: {}", id, tickerJson);

        Ticker.Builder tickerBuilder = Ticker.newBuilder();

        ExchangeNode exchangeNode;

        // Check if exchange information is present in the JSON string
        if (objectMapper.readTree(tickerJson).has("exchange")) {

            // Parse the JSON string and get Exchange information
            String exchangeJson = objectMapper.readTree(tickerJson).get("exchange").toString();

            // Create a new ExchangeProto from the exchange json information
            Exchange.Builder exchangeBuilder = Exchange.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(exchangeJson, exchangeBuilder);
            Exchange exchangeProto = exchangeBuilder.build();

            // Create a new ExchangeNode from the exchange information
            String exchangeCode = exchangeProto.getCode();
            String exchangeName = exchangeProto.getName();
            String exchangeCountry = exchangeProto.getCountry();
            exchangeNode = new ExchangeNode(exchangeCode, exchangeName, exchangeCountry);

        } else {
            exchangeNode = null;
        }

        // Parse the JSON string into a Ticker message
        JsonFormat.parser().ignoringUnknownFields().merge(tickerJson, tickerBuilder);

        return tickerRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(tickerBuilder.getSymbol());
                    existing.setName(tickerBuilder.getName());
                    existing.setExchange(exchangeNode);
                    existing.setTimestamp(tickerBuilder.getTimestamp());
                    return existing;
                })
                .flatMap(tickerRepository::saveWithRetry);
    }

    @Override
    public Mono<TickerNode> updateTicker(String id, Ticker tickerProto) {
        log.debug("Updating ticker with id: {} from protobuf message: {}", id, tickerProto);

        ExchangeNode exchangeNode = exchangeService.getExchangeNodeFromProto(tickerProto.getExchange());

        return tickerRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(tickerProto.getSymbol());
                    existing.setName(tickerProto.getName());
                    existing.setExchange(exchangeNode);
                    existing.setTimestamp(tickerProto.getTimestamp());
                    return existing;
                })
                .flatMap(tickerRepository::save);
    }

    @Override
    public Mono<Void> deleteTicker(String id) {
        log.debug("Deleting ticker with id: {}", id);

        return tickerRepository.deleteById(id);
    }

    @Override
    public Mono<TickerNode> getTickerBySymbol(String symbol) {
        log.debug("Getting ticker by symbol: {}", symbol);

        return tickerRepository.findBySymbol(symbol);
    }

}


