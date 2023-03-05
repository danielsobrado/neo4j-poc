package com.jds.neo4j.reactive.service;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.ExchangeProto;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.google.protobuf.util.JsonFormat.parser;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService{
    private final TradeRepository tradeRepository;

    public Flux<TradeNode> getAllTrades() {
        log.debug("Getting all trades");
        return tradeRepository.findAll();
    }

    public Mono<TradeNode> getTradeById(Long id) {
        log.debug("Getting trade by id: {}", id);
        return tradeRepository.findById(id);
    }

    public Mono<TradeNode> createTrade(String tradeJson) throws InvalidProtocolBufferException {
        TradeProto.Trade.Builder tradeBuilder = TradeProto.Trade.newBuilder();

        // Parse the JSON string into a Trade message
        parser().ignoringUnknownFields().merge(tradeJson, tradeBuilder);

        // Call the createTradeFromProto method with the parsed Trade message
        return createTrade(tradeBuilder.build());
    }

    public Mono<TradeNode> createTrade(TradeProto.Trade tradeProto) {
        // Extract the exchange information from the Trade message
        ExchangeProto.Exchange exchange = tradeProto.getExchange();
        String exchangeCode = exchange.getCode();
        String exchangeName = exchange.getName();
        String exchangeCountry = exchange.getCountry();

        // Create a new ExchangeNode from the exchange information
        ExchangeNode exchangeNode = new ExchangeNode(exchangeCode, exchangeName, exchangeCountry);

        // Create a new TradeNode from the trade information and the ExchangeNode
        TradeNode tradeNode = new TradeNode(
                tradeProto.getSymbol(),
                tradeProto.getPrice(),
                tradeProto.getQuantity(),
                tradeProto.getSide(),
                exchangeNode,
                tradeProto.getTimestamp()
        );

        return tradeRepository.save(tradeNode);
    }

    public Mono<TradeNode> updateTrade(Long id, String tradeJson) throws InvalidProtocolBufferException {
        TradeProto.Trade.Builder tradeBuilder = TradeProto.Trade.newBuilder();

        // Parse the JSON string into a Trade message
        JsonFormat.parser().ignoringUnknownFields().merge(tradeJson, tradeBuilder);

        // Extract the exchange information from the Trade message
        ExchangeProto.Exchange exchange = tradeBuilder.getExchange();
        String exchangeCode = exchange.getCode();
        String exchangeName = exchange.getName();
        String exchangeCountry = exchange.getCountry();

        // Create a new ExchangeNode from the exchange information
        ExchangeNode exchangeNode = new ExchangeNode(exchangeCode, exchangeName, exchangeCountry);

        // Create a new TradeNode from the trade information and the ExchangeNode
        TradeNode tradeNode = new TradeNode(
                tradeBuilder.getSymbol(),
                tradeBuilder.getPrice(),
                tradeBuilder.getQuantity(),
                tradeBuilder.getSide(),
                exchangeNode,
                tradeBuilder.getTimestamp()
        );

        return updateTrade(id, tradeNode);
    }

    public Mono<TradeNode> updateTrade(Long id, TradeProto.Trade tradeProto) {
        // Extract the exchange information from the Trade message
        ExchangeProto.Exchange exchange = tradeProto.getExchange();
        String exchangeCode = exchange.getCode();
        String exchangeName = exchange.getName();
        String exchangeCountry = exchange.getCountry();

        // Create a new ExchangeNode from the exchange information
        ExchangeNode exchangeNode = new ExchangeNode(exchangeCode, exchangeName, exchangeCountry);

        // Create a new TradeNode from the trade information and the ExchangeNode
        TradeNode tradeNode = new TradeNode(
                tradeProto.getSymbol(),
                tradeProto.getPrice(),
                tradeProto.getQuantity(),
                tradeProto.getSide(),
                exchangeNode,
                tradeProto.getTimestamp()
        );

        return updateTrade(id, tradeNode);
    }

    private Mono<TradeNode> updateTrade(Long id, TradeNode tradeNode) {
        log.debug("Updating trade with id: {}, data: {}", id, tradeNode);
        return tradeRepository.findById(id)
                .map(existing -> {
                    existing.setSymbol(tradeNode.getSymbol());
                    existing.setPrice(tradeNode.getPrice());
                    existing.setQuantity(tradeNode.getQuantity());
                    existing.setSide(tradeNode.getSide());
                    existing.setExchange(tradeNode.getExchange());
                    existing.setTimestamp(tradeNode.getTimestamp());
                    return existing;
                })
                .flatMap(tradeRepository::save);
    }


    public Mono<Void> deleteTrade(Long id) {
        log.debug("Deleting trade with id: {}", id);
        return tradeRepository.deleteById(id);
    }
}



