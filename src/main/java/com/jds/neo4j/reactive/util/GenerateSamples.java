package com.jds.neo4j.reactive.util;

import com.jds.neo4j.reactive.graphs.model.*;
import com.jds.neo4j.reactive.model.TradeProto;
import com.jds.neo4j.reactive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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

    // Store currencies, exchanges, tickers, and traders as class members
    private List<CurrencyNode> currencies;
    private List<ExchangeNode> exchanges;
    private List<TickerNode> tickers;
    private List<TraderNode> traders;

    public void generateSampleData() {
        // Generate currency JSONs
        currencies = generateCurrencyJSONs();

        // Generate exchange JSONs
        exchanges = generateExchangeJSONs();

        // Generate ticker JSONs
        tickers = generateTickerJSONs();

        // Generate trader JSONs
        traders = generateTraderJSONs();

        // Generate price JSONs
        generatePriceJSONs();

        // Generate ETF JSONs
        generateETFJSONs();

        // Generate Index JSONs
        generateIndexJSONs();

        // Generate spinoff JSONs
        generateSpinoffJSONs();

        // Generate trade JSONs
        generateTradeJSONs();
    }

    private List<CurrencyNode> generateCurrencyJSONs() {
        List<CurrencyNode> currencies = new ArrayList<>();

        currencies.add(new CurrencyNode("USD", "US Dollar", "$"));
        currencies.add(new CurrencyNode("EUR", "Euro", "€"));
        currencies.add(new CurrencyNode("GBP", "British Pound", "£"));

        currencyRepository.saveAllWithRetry(currencies).subscribe();
        return currencies;
    }

    private List<ExchangeNode> generateExchangeJSONs() {
        List<ExchangeNode> exchanges = new ArrayList<>();

        CurrencyNode usd = currencies.stream().filter(c -> c.getCode().equals("USD")).findFirst().orElse(null);
        CurrencyNode gbp = currencies.stream().filter(c -> c.getCode().equals("GBP")).findFirst().orElse(null);

        exchanges.add(new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA", usd));
        exchanges.add(new ExchangeNode("NYSE", "New York Stock Exchange", "USA", usd));
        exchanges.add(new ExchangeNode("LSE", "London Stock Exchange", "UK", gbp));

        exchangeRepository.saveAllWithRetry(exchanges).subscribe();
        return exchanges;
    }

    private List<TickerNode> generateTickerJSONs() {
        List<TickerNode> tickers = new ArrayList<>();

        ExchangeNode nasdaq = exchanges.stream().filter(e -> e.getCode().equals("NASDAQ")).findFirst().orElse(null);
        ExchangeNode nyse = exchanges.stream().filter(e -> e.getCode().equals("NYSE")).findFirst().orElse(null);

        tickers.add(new TickerNode("AAPL", "Apple Inc.", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("GOOGL", "Alphabet Inc.", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("AMZN", "Amazon.com, Inc.", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("MSFT", "Microsoft Corporation", nasdaq, System.currentTimeMillis()));
        tickers.add(new TickerNode("IBM", "International Business Machines Corporation", nyse, System.currentTimeMillis()));
        tickerRepository.saveAllWithRetry(tickers).subscribe();
        return tickers;
    }

    private List<TraderNode> generateTraderJSONs() {
        List<TraderNode> traders = new ArrayList<>();

        PortfolioNode portfolio1 = new PortfolioNode("Investments");
        PortfolioNode portfolio2 = new PortfolioNode("Dividend Portfolio");
        PortfolioNode portfolio3 = new PortfolioNode("Long Term Holdings");

        traders.add(new TraderNode("Alice", 100000.0, new HashSet<>(List.of(portfolio1))));
        traders.add(new TraderNode("Bob", 150000.0, new HashSet<>(List.of(portfolio2))));
        traders.add(new TraderNode("Charlie", 200000.0, new HashSet<>(List.of(portfolio3))));

        traderRepository.saveAllWithRetry(traders).subscribe();
        return traders;
    }

    private void generatePriceJSONs() {
        List<PriceNode> prices = new ArrayList<>();
        CurrencyNode usd = currencies.stream().filter(c -> c.getCode().equals("USD")).findFirst().orElse(null);
        ExchangeNode nasdaq = exchanges.stream().filter(e -> e.getCode().equals("NASDAQ")).findFirst().orElse(null);
        TickerNode aapl = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);

        prices.add(new PriceNode(aapl, 145.0, 147.0, 144.0, 146.0, 12000000.0, System.currentTimeMillis()));

        priceRepository.saveAllWithRetry(prices).subscribe();
    }

    private void generateETFJSONs() {
        List<ETFNode> etfs = new ArrayList<>();
        ExchangeNode nasdaq = exchanges.stream().filter(e -> e.getCode().equals("NASDAQ")).findFirst().orElse(null);

        TickerNode aapl = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TickerNode msft = tickers.stream().filter(t -> t.getSymbol().equals("MSFT")).findFirst().orElse(null);
        TickerNode amzn = tickers.stream().filter(t -> t.getSymbol().equals("AMZN")).findFirst().orElse(null);

        ETFComponent component1 = new ETFComponent(aapl, 0.45);
        ETFComponent component2 = new ETFComponent(msft, 0.3);
        ETFComponent component3 = new ETFComponent(amzn, 0.25);

        ETFNode etf1 = new ETFNode("ETFX", "Sample ETF");
        etf1.setComponents(Arrays.asList(component1, component2, component3));

        etfs.add(etf1);

        etfRepository.saveAllWithRetry(etfs).subscribe();
    }

    private void generateIndexJSONs() {
        List<IndexNode> indices = new ArrayList<>();
        ExchangeNode nasdaq = exchanges.stream().filter(e -> e.getCode().equals("NASDAQ")).findFirst().orElse(null);

        TickerNode aapl = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TickerNode msft = tickers.stream().filter(t -> t.getSymbol().equals("MSFT")).findFirst().orElse(null);
        TickerNode amzn = tickers.stream().filter(t -> t.getSymbol().equals("AMZN")).findFirst().orElse(null);

        IndexComponent component1 = new IndexComponent(aapl, 0.45);
        IndexComponent component2 = new IndexComponent(msft, 0.3);
        IndexComponent component3 = new IndexComponent(amzn, 0.25);

        IndexNode index1 = new IndexNode("INDX", "Sample Index");
        index1.setComponents(Arrays.asList(component1, component2, component3));

        indices.add(index1);

        indexRepository.saveAllWithRetry(indices).subscribe();
    }

    private void generateSpinoffJSONs() {
        List<Spinoff> spinoffs = new ArrayList<>();

        TickerNode aapl = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TickerNode appl = tickers.stream().filter(t -> t.getSymbol().equals("APPL")).findFirst().orElse(null);
        ExchangeNode nasdaq = exchanges.stream().filter(e -> e.getCode().equals("NASDAQ")).findFirst().orElse(null);
        Spinoff spinoff1 = new Spinoff(aapl, appl, System.currentTimeMillis());
        spinoffs.add(spinoff1);

        spinoffRepository.saveAllWithRetry(spinoffs).subscribe();
    }

    private void generateTradeJSONs() {
        List<TradeNode> trades = new ArrayList<>();

        TickerNode ticker1 = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TraderNode trader1 = traders.stream().filter(t -> t.getName().equals("John Doe")).findFirst().orElse(null);
        if (trader1 == null) {
            trader1 = new TraderNode("John Doe");
            traders.add(trader1);
            traderRepository.saveWithRetry(trader1).subscribe();
        }
        PriceNode priceNode = new PriceNode(ticker1, 120.0, System.currentTimeMillis());
        TradeNode trade1 = new TradeNode(ticker1, priceNode, 100L, TradeProto.Side.BUY, System.currentTimeMillis(), trader1);
        trades.add(trade1);

        // Save all trades
        tradeRepository.saveAllWithRetry(trades).subscribe();
    }
}
