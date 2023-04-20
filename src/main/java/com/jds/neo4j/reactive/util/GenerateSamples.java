package com.jds.neo4j.reactive.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jds.neo4j.reactive.graphs.model.*;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        currencyRepository.saveAllWithRetry(currencies).subscribe();
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

        prices.add(new PriceNode(aapl, 145.0, 147.0, 144.0, 146.0, 12000000.0, System.currentTimeMillis()));

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

        ETFComponent component1 = new ETFComponent(aapl, 0.45);
        ETFComponent component2 = new ETFComponent(msft, 0.3);
        ETFComponent component3 = new ETFComponent(amzn, 0.25);

        ETFNode etf1 = new ETFNode("ETFX", "Sample ETF");
        etf1.setComponents(Arrays.asList(component1, component2, component3));

        etfs.add(etf1);

        etfRepository.saveAll(etfs).subscribe();
    }

    private void generateIndexJSONs() {
        List<IndexNode> indices = new ArrayList<>();
        ExchangeNode nasdaq = new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA");

        TickerNode aapl = new TickerNode("AAPL", "Apple Inc.", nasdaq, System.currentTimeMillis());
        TickerNode msft = new TickerNode("MSFT", "Microsoft Corporation", nasdaq, System.currentTimeMillis());
        TickerNode amzn = new TickerNode("AMZN", "Amazon.com, Inc.", nasdaq, System.currentTimeMillis());

        IndexComponent component1 = new IndexComponent(aapl, 0.45);
        IndexComponent component2 = new IndexComponent(msft, 0.3);
        IndexComponent component3 = new IndexComponent(amzn, 0.25);

        IndexNode index1 = new IndexNode("INDX", "Sample Index");
        index1.setComponents(Arrays.asList(component1, component2, component3));

        indices.add(index1);

        indexRepository.saveAll(indices).subscribe();
    }


    private void generateSpinoffJSONs() {
        List<Spinoff> spinoffs = new ArrayList<>();
        
        Spinoff spinoff1 = new Spinoff(new TickerNode("AAPL", "Apple Inc.", new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA"), System.currentTimeMillis()), new TickerNode("APPL", "Apple Inc.", new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA"), System.currentTimeMillis()), System.currentTimeMillis());
        spinoffs.add(spinoff1);

        spinoffRepository.saveAll(spinoffs).subscribe();
    }


    private void generateTradeJSONs() {
        List<TradeNode> trades = new ArrayList<>();

        TickerNode ticker1 = new TickerNode("AAPL", "Apple Inc.", new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA"), System.currentTimeMillis());
        TradeNode trade1 = new TradeNode(ticker1, 120.0, 100L, TradeProto.Side.BUY, System.currentTimeMillis());
        trades.add(trade1);

        tradeRepository.saveAll(trades).subscribe();
    }

}
