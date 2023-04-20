package com.jds.neo4j.reactive.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jds.neo4j.reactive.graphs.model.*;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GenerateSamples {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private ETFRepository etfRepository;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private SpinoffRepository spinoffRepository;

    @Autowired
    private TradeRepository tradeRepository;

    public void generateSampleData() {

        // Generate currency JSONs
        generateCurrencyJSONs();

        // Generate ETF JSONs
        generateETFJSONs();

        // Generate Index JSONs
        generateIndexJSONs();

        // Generate exchange JSONs
        generateExchangeJSONs();

        // Generate price JSONs
        generatePriceJSONs();

        // Generate spinoff JSONs
        generateSpinoffJSONs();

        // Generate ticker JSONs
        generateTickerJSONs();

        // Generate trade JSONs
        generateTradeJSONs();

        // Generate trader JSONs
        generateTraderJSONs();
    }

    private void generateCurrencyJSONs() {
        List<CurrencyNode> currencies = new ArrayList<>();

        currencies.add(new CurrencyNode("USD", "US Dollar", "$"));
        currencies.add(new CurrencyNode("EUR", "Euro", "€"));
        currencies.add(new CurrencyNode("GBP", "British Pound", "£"));

        ObjectMapper objectMapper = new ObjectMapper();

        currencyRepository.saveAll(currencies).subscribe();
    }

    private void generateTickerJSONs() {
        List<TickerNode> tickers = new ArrayList<>();
        ExchangeNode nasdaq = new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA");
        ExchangeNode nyse = new ExchangeNode("NYSE", "New York Stock Exchange", "USA");

        tickers.add(new TickerNode("AAPL", "Apple Inc.", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("GOOGL", "Alphabet Inc.", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("AMZN", "Amazon.com, Inc.", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("MSFT", "Microsoft Corporation", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("IBM", "International Business Machines Corporation", nyse, System.currentTimeMillis()));

        tickerRepository.saveAll(tickers).subscribe();
    }

    private void generateExchangeJSONs() {
        List<ExchangeNode> exchanges = new ArrayList<>();

        exchanges.add(new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA"));
        exchanges.add(new ExchangeNode("NYSE", "New York Stock Exchange", "USA"));
        exchanges.add(new ExchangeNode("LSE", "London Stock Exchange", "UK"));

        exchangeRepository.saveAll(exchanges).subscribe();
    }

    private void generatePriceJSONs() {
        List<PriceNode> prices = new ArrayList<>();
        CurrencyNode usd = new CurrencyNode("USD", "US Dollar", "$");
        ExchangeNode nasdaq = new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA");
        TickerNode aapl = new TickerNode("AAPL", "Apple Inc.", nasdaq, System.currentTimeMillis());

        prices.add(new PriceNode(aapl, 145.0, 147.0, 144.0, 146.0, 12000000.0, usd, nasdaq, System.currentTimeMillis()));

        priceRepository.saveAll(prices).subscribe();
    }

    private void generateTraderJSONs() {
        List<TraderNode> traders = new ArrayList<>();

        traders.add(new TraderNode("Alice", 100000.0, null));
        traders.add(new TraderNode("Bob", 150000.0, null));
        traders.add(new TraderNode("Charlie", 200000.0, null));

        traderRepository.saveAll(traders).subscribe();
    }

    private void generateETFJSONs() {
        List<ETFNode> etfs = new ArrayList<>();
        ExchangeNode nasdaq = new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA");

        TickerNode aapl = new TickerNode("AAPL", "Apple Inc.", nasdaq, System.currentTimeMillis());
        TickerNode msft = new TickerNode("MSFT", "Microsoft Corporation", nasdaq, System.currentTimeMillis());
        TickerNode amzn = new TickerNode("AMZN", "Amazon.com, Inc.", nasdaq, System.currentTimeMillis());

        ETFComponentNode component1 = new ETFComponentNode("1", null, aapl, 0.45);
        ETFComponentNode component2 = new ETFComponentNode("2", null, msft, 0.3);
        ETFComponentNode component3 = new ETFComponentNode("3", null, amzn, 0.25);

        ETFNode etf1 = new ETFNode("ETFX", "Sample ETF");
        etf1.setComponents(Arrays.asList(component1, component2, component3));
        component1.setEtf(etf1);
        component2.setEtf(etf1);
        component3.setEtf(etf1);

        etfs.add(etf1);

        etfRepository.saveAll(etfs).subscribe();
    }

    private void generateIndexJSONs() {
        List<IndexNode> indices = new ArrayList<>();
        ExchangeNode nasdaq = new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA");

        TickerNode aapl = new TickerNode("AAPL", "Apple Inc.", nasdaq, System.currentTimeMillis());
        TickerNode msft = new TickerNode("MSFT", "Microsoft Corporation", nasdaq, System.currentTimeMillis());
        TickerNode amzn = new TickerNode("AMZN", "Amazon.com, Inc.", nasdaq, System.currentTimeMillis());

        IndexComponentNode component1 = new IndexComponentNode("1", null, aapl, 0.45);
        IndexComponentNode component2 = new IndexComponentNode("2", null, msft, 0.3);
        IndexComponentNode component3 = new IndexComponentNode("3", null, amzn, 0.25);

        IndexNode index1 = new IndexNode("INDX", "Sample Index");
        index1.setComponents(Arrays.asList(component1, component2, component3));
        component1.setIndex(index1);
        component2.setIndex(index1);
        component3.setIndex(index1);

        indices.add(index1);

        indexRepository.saveAll(indices).subscribe();
    }

    private void generateSpinoffJSONs() {
        List<SpinoffNode> spinoffs = new ArrayList<>();

        SpinoffNode spinoff1 = new SpinoffNode("1", "PARENT1", "SPINOFF1", LocalDate.of(2023, 1, 1));
        spinoffs.add(spinoff1);

        spinoffRepository.saveAll(spinoffs).subscribe();
    }

    private void generateTradeJSONs() {
        List<TradeNode> trades = new ArrayList<>();

        TickerNode ticker1 = new TickerNode("AAPL");
        TradeNode trade1 = new TradeNode(ticker1, 120.0, 100L, TradeProto.Side.BUY, System.currentTimeMillis());
        trades.add(trade1);

        tradeRepository.saveAll(trades).subscribe();
    }

}
