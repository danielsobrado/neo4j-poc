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
        CurrencyNode eur = currencies.stream().filter(c -> c.getCode().equals("EUR")).findFirst().orElse(null);

        exchanges.add(new ExchangeNode("NASDAQ", "NASDAQ Stock Market", "USA", usd));
        exchanges.add(new ExchangeNode("NYSE", "New York Stock Exchange", "USA", usd));
        exchanges.add(new ExchangeNode("LSE", "London Stock Exchange", "UK", gbp));
        exchanges.add(new ExchangeNode("EURONEXT", "Euronext", "EU", eur));

        exchangeRepository.saveAllWithRetry(exchanges).subscribe();
        return exchanges;
    }

    private List<TickerNode> generateTickerJSONs() {
        List<TickerNode> tickers = new ArrayList<>();

        ExchangeNode nasdaq = exchanges.stream().filter(e -> e.getCode().equals("NASDAQ")).findFirst().orElse(null);
        ExchangeNode nyse = exchanges.stream().filter(e -> e.getCode().equals("NYSE")).findFirst().orElse(null);
        ExchangeNode lse = exchanges.stream().filter(e -> e.getCode().equals("LSE")).findFirst().orElse(null);
        ExchangeNode euronext = exchanges.stream().filter(e -> e.getCode().equals("EURONEXT")).findFirst().orElse(null);

        tickers.add(new TickerNode("AAPL", "Apple Inc.", nasdaq));
        tickers.add(new TickerNode("MSFT", "Microsoft Corporation", nasdaq));
        tickers.add(new TickerNode("GOOG", "Alphabet Inc.", nasdaq));
        tickers.add(new TickerNode("AMZN", "Amazon.com, Inc.", nasdaq));
        tickers.add(new TickerNode("V", "Visa Inc.", nyse));
        tickers.add(new TickerNode("BP", "BP plc", lse));
        tickers.add(new TickerNode("RDSA", "Royal Dutch Shell plc", lse));
        tickers.add(new TickerNode("SAN", "Banco Santander, S.A.", euronext));
        tickers.add(new TickerNode("TEF", "Telefónica, S.A.", euronext));

        tickerRepository.saveAllWithRetry(tickers).subscribe();
        return tickers;
    }

    private List<TraderNode> generateTraderJSONs() {
        List<TraderNode> traders = new ArrayList<>();

        PortfolioNode portfolio1 = new PortfolioNode("Investments");
        PortfolioNode portfolio2 = new PortfolioNode("Dividend Portfolio");
        PortfolioNode portfolio3 = new PortfolioNode("Long Term Holdings");
        PortfolioNode portfolio4 = new PortfolioNode("Tech Stocks");
        PortfolioNode portfolio5 = new PortfolioNode("Blue Chips");

        traders.add(new TraderNode("Alice", 100000.0, new HashSet<>(List.of(portfolio1, portfolio2))));
        traders.add(new TraderNode("Bob", 150000.0, new HashSet<>(List.of(portfolio3))));
        traders.add(new TraderNode("Charlie", 200000.0, new HashSet<>(List.of(portfolio4, portfolio5))));

        traderRepository.saveAllWithRetry(traders).subscribe();
        return traders;
    }

    private void generatePriceJSONs() {
        List<PriceNode> prices = new ArrayList<>();

        TickerNode aapl = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TickerNode googl = tickers.stream().filter(t -> t.getSymbol().equals("GOOGL")).findFirst().orElse(null);
        TickerNode amzn = tickers.stream().filter(t -> t.getSymbol().equals("AMZN")).findFirst().orElse(null);
        TickerNode msft = tickers.stream().filter(t -> t.getSymbol().equals("MSFT")).findFirst().orElse(null);
        TickerNode ibm = tickers.stream().filter(t -> t.getSymbol().equals("IBM")).findFirst().orElse(null);

        prices.add(new PriceNode(aapl, 140.0, 142.0, 139.0, 141.0, 15000000.0, System.currentTimeMillis()));
        prices.add(new PriceNode(aapl, 141.0, 143.0, 140.0, 142.0, 16000000.0, System.currentTimeMillis() - 86400000));
        prices.add(new PriceNode(aapl, 139.0, 141.0, 138.0, 140.0, 17000000.0, System.currentTimeMillis() - 172800000));

        prices.add(new PriceNode(googl, 1000.0, 1020.0, 990.0, 1010.0, 10000000.0, System.currentTimeMillis()));
        prices.add(new PriceNode(googl, 1010.0, 1030.0, 1000.0, 1020.0, 12000000.0, System.currentTimeMillis() - 86400000));
        prices.add(new PriceNode(googl, 990.0, 1010.0, 980.0, 1000.0, 11000000.0, System.currentTimeMillis() - 172800000));

        prices.add(new PriceNode(amzn, 2000.0, 2020.0, 1980.0, 2010.0, 9000000.0, System.currentTimeMillis()));
        prices.add(new PriceNode(amzn, 2010.0, 2030.0, 1990.0, 2020.0, 10000000.0, System.currentTimeMillis() - 86400000));
        prices.add(new PriceNode(amzn, 1980.0, 2000.0, 1960.0, 1990.0, 8000000.0, System.currentTimeMillis() - 172800000));

        prices.add(new PriceNode(msft, 110.0, 112.0, 109.0, 111.0, 14000000.0, System.currentTimeMillis()));
        prices.add(new PriceNode(msft, 111.0, 113.0, 110.0, 112.0, 15000000.0, System.currentTimeMillis() - 86400000));
        prices.add(new PriceNode(msft, 109.0, 111.0, 108.0, 110.0, 13000000.0, System.currentTimeMillis() - 172800000));

        prices.add(new PriceNode(ibm, 120.0, 122.0, 119.0, 121.0, 11000000.0, System.currentTimeMillis()));
        prices.add(new PriceNode(ibm, 121.0, 123.0, 120.0, 122.0, 12000000.0, System.currentTimeMillis() - 86400000));
        prices.add(new PriceNode(ibm, 119.0, 121.0, 118.0, 120.0, 10000000.0, System.currentTimeMillis() - 172800000));

        priceRepository.saveAllWithRetry(prices).subscribe();
    }

    private void generateETFJSONs() {
        List<ETFNode> etfs = new ArrayList<>();

        TickerNode aapl = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TickerNode msft = tickers.stream().filter(t -> t.getSymbol().equals("MSFT")).findFirst().orElse(null);
        TickerNode amzn = tickers.stream().filter(t -> t.getSymbol().equals("AMZN")).findFirst().orElse(null);

        ETFComponent component1 = new ETFComponent(aapl, 0.45);
        ETFComponent component2 = new ETFComponent(msft, 0.3);
        ETFComponent component3 = new ETFComponent(amzn, 0.25);

        ETFNode etf1 = new ETFNode("ETFX", "Technology Select Sector ETF");
        etf1.setComponents(Arrays.asList(component1, component2, component3));
        etfs.add(etf1);

        ETFNode etf2 = new ETFNode("ETFY", "Consumer Discretionary Select Sector ETF");
        etf2.setComponents(Arrays.asList(component1, component2, component3));
        etfs.add(etf2);

        etfRepository.saveAllWithRetry(etfs).subscribe();
    }

    private void generateIndexJSONs() {
        IndexNode ibex35 = new IndexNode("Ibex 35", "Spanish stock market index");

        TickerNode san = tickers.stream().filter(t -> t.getSymbol().equals("SAN")).findFirst().orElse(null);
        TickerNode tef = tickers.stream().filter(t -> t.getSymbol().equals("TEF")).findFirst().orElse(null);

        ibex35.addTicker(san, 10L);
        ibex35.addTicker(tef, 5L);

        indexRepository.saveWithRetry(ibex35).subscribe();
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

        // First set of trades
        TraderNode bob = traders.stream().filter(t -> t.getName().equals("Bob")).findFirst().orElse(null);
        TickerNode san = tickers.stream().filter(t -> t.getSymbol().equals("SAN")).findFirst().orElse(null);
        TickerNode tef = tickers.stream().filter(t -> t.getSymbol().equals("TEF")).findFirst().orElse(null);

        trades.add(new TradeNode(bob, san, 100, 3.5));
        trades.add(new TradeNode(bob, tef, 200, 4.2));

        // Second set of trades
        TickerNode ticker1 = tickers.stream().filter(t -> t.getSymbol().equals("AAPL")).findFirst().orElse(null);
        TraderNode trader1 = traders.stream().filter(t -> t.getName().equals("Alice")).findFirst().orElse(null);
        if (trader1 == null) {
            trader1 = new TraderNode("Alice");
            traders.add(trader1);
            traderRepository.saveWithRetry(trader1).subscribe();
        }
        PriceNode priceNode = new PriceNode(ticker1, 120.0, System.currentTimeMillis());
        TradeNode trade1 = new TradeNode(ticker1, priceNode, 100L, TradeProto.Side.BUY, System.currentTimeMillis(), trader1);
        trades.add(trade1);

        PortfolioNode portfolio1 = trader1.getPortfolios().stream().findFirst().orElse(null);
        if (portfolio1 != null) {
            portfolio1.addTicker(ticker1, 100L);
        }

        TickerNode ticker2 = tickers.stream().filter(t -> t.getSymbol().equals("MSFT")).findFirst().orElse(null);
        TraderNode trader2 = traders.stream().filter(t -> t.getName().equals("Bob")).findFirst().orElse(null);
        PriceNode priceNode2 = new PriceNode(ticker2, 110.0, System.currentTimeMillis());
        TradeNode trade2 = new TradeNode(ticker2, priceNode2, 200L, TradeProto.Side.BUY, System.currentTimeMillis(), trader2);
        trades.add(trade2);

        PortfolioNode portfolio2 = trader2.getPortfolios().stream().findFirst().orElse(null);
        if (portfolio2 != null) {
            portfolio2.addTicker(ticker2, 200L);
        }

        // Save all trades
        tradeRepository.saveAllWithRetry(trades).subscribe();
    }

}
