package com.jds.neo4j.reactive.service;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.jds.neo4j.reactive.graphs.model.ExchangeNode;
import com.jds.neo4j.reactive.graphs.model.TradeNode;
import com.jds.neo4j.reactive.model.ExchangeProto.Exchange;
import com.jds.neo4j.reactive.model.TradeProto.Trade;
import com.jds.neo4j.reactive.repository.TradeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.google.protobuf.util.JsonFormat.parser;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    @NonNull
    private final TradeRepository tradeRepository;

    @NonNull
    private final ExchangeService exchangeService;

    static Exchange createExchangeProto(TradeNode tradeNode) {

        // Create a new Exchange message from the ExchangeNode
        return Exchange.newBuilder()
                .setCode(tradeNode.getExchange().getCode())
                .setName(tradeNode.getExchange().getName())
                .setCountry(tradeNode.getExchange().getCountry())
                .build();
    }

    @Override
    public Flux<TradeNode> getAllTrades() {
        log.debug("Getting all trades");

        return tradeRepository.findAll();
    }

    @Override
    public Mono<TradeNode> getTradeById(Long id) {
        log.debug("Getting trade by id: {}", id);

        return tradeRepository.findById(id);
    }

    @Override
    public Mono<TradeNode> createTrade(String tradeJson) throws InvalidProtocolBufferException {
        log.debug("Creating trade: {}", tradeJson);

        Trade.Builder tradeBuilder = Trade.newBuilder();

        // Parse the JSON string into a Trade message
        parser().ignoringUnknownFields().merge(tradeJson, tradeBuilder);

        // Call the createTradeFromProto method with the parsed Trade message
        return createTrade(tradeBuilder.build());
    }

    @Override
    public Mono<TradeNode> createTrade(Trade tradeProto) {
        log.debug("Creating trade: {}", tradeProto);

        // Convert the Trade message to a TradeNode object
        TradeNode tradeNode = convertToNode(tradeProto);

        return tradeRepository.save(tradeNode);
    }

    @Override
    public Mono<TradeNode> updateTrade(Long id, String tradeJson) throws InvalidProtocolBufferException {
        log.debug("Updating trade with id: {}, data: {}", id, tradeJson);

        Trade.Builder tradeBuilder = Trade.newBuilder();

        // Parse the JSON string into a Trade message
        JsonFormat.parser().ignoringUnknownFields().merge(tradeJson, tradeBuilder);

        ExchangeNode exchangeNode = exchangeService.createExchangeNode(tradeBuilder.getExchange());

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

    @Override
    public Mono<TradeNode> updateTrade(Long id, Trade tradeProto) {
        log.debug("Updating trade with id: {}, data: {}", id, tradeProto);

        // Convert the Trade message to a TradeNode object
        TradeNode tradeNode = convertToNode(tradeProto);

        return updateTrade(id, tradeNode);
    }

    @Override
    public Mono<TradeNode> updateTrade(Long id, TradeNode tradeNode) {
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

    @Override
    public Mono<Void> deleteTrade(Long id) {
        log.debug("Deleting trade with id: {}", id);

        return tradeRepository.deleteById(id);
    }

    @Override
    public TradeNode convertToNode(Trade trade) {
        log.debug("Converting trade to node: {}", trade);

        // Create an ExchangeNode from the exchange information
        ExchangeNode exchangeNode = exchangeService.createExchangeNode(trade.getExchange());

        // Create a new TradeNode from the trade information and the ExchangeNode
        return new TradeNode(
                trade.getSymbol(),
                trade.getPrice(),
                trade.getQuantity(),
                trade.getSide(),
                exchangeNode,
                trade.getTimestamp()
        );
    }

    @Override
    public Trade convertToProto(TradeNode tradeNode) {
        log.debug("Converting trade to proto: {}", tradeNode);

        Exchange exchange = createExchangeProto(tradeNode);

        // Create a new Trade message from the trade information and the Exchange message
        return Trade.newBuilder()
                .setSymbol(tradeNode.getSymbol())
                .setPrice(tradeNode.getPrice())
                .setQuantity(tradeNode.getQuantity())
                .setSide(tradeNode.getSide())
                .setExchange(exchange)
                .setTimestamp(tradeNode.getTimestamp())
                .build();
    }

}



