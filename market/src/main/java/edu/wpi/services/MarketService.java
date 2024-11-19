package edu.wpi.services;

import edu.wpi.entities.ExchangeCoin;
import edu.wpi.entities.CoinThumb;
import edu.wpi.entities.ExchangeTrade;
import edu.wpi.entities.KLine;
import edu.wpi.exceptions.PriceNotFoundException;
import edu.wpi.handler.KrakenWebSocketListener;
import edu.wpi.repositories.ExchangeCoinRepository;
import edu.wpi.repositories.ExchangeTradeRepository;
import edu.wpi.repositories.KLineRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MarketService {
    private static final Logger logger = LoggerFactory.getLogger(MarketService.class);

    private final KLineRepository kLineRepository;
    private final ExchangeCoinRepository exchangeCoinRepository;
    private final ExchangeTradeRepository exchangeTradeRepository;
    private boolean isWebSocketConnected = false;
    private WebSocket webSocket;
    private final OkHttpClient client = new OkHttpClient();

    // Add constants for trading pairs
    private static final String[] TRADING_PAIRS = {
        "XBT/USD",  // Bitcoin
        "ETH/USD",  // Ethereum
        "LTC/USD"   // Litecoin
    };
    public BigDecimal getCurrentPrice(String symbol) {
        KLine latestKLine = kLineRepository.findFirstBySymbolOrderByTimeDesc(symbol)
                .orElseThrow(() -> new PriceNotFoundException(
                        "No price data available for " + symbol));
        return latestKLine.getClosePrice();
    }
    public MarketService(KLineRepository kLineRepository, ExchangeCoinRepository exchangeCoinRepository,
                         ExchangeTradeRepository exchangeTradeRepository) {
        this.kLineRepository = kLineRepository;
        this.exchangeCoinRepository = exchangeCoinRepository;
        this.exchangeTradeRepository = exchangeTradeRepository;
    }

    public boolean isWebSocketConnected() {
        return isWebSocketConnected;
    }

    @PostConstruct
    public void connectWebSocket() {
        String webSocketUrl = "wss://ws.kraken.com";

        Request request = new Request.Builder()
                .url(webSocketUrl)
                .build();

        KrakenWebSocketListener listener = new KrakenWebSocketListener(kLineRepository,exchangeCoinRepository) {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                super.onOpen(webSocket, response);
                isWebSocketConnected = true;
                logger.info("WebSocket connection status: {}", isWebSocketConnected);
                subscribeToKline(webSocket);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                isWebSocketConnected = false;
                logger.info("WebSocket connection status: {}", isWebSocketConnected);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                super.onFailure(webSocket, t, response);
                isWebSocketConnected = false;
                logger.error("WebSocket connection failed: ", t);
            }
        };

        webSocket = client.newWebSocket(request, listener);
        logger.info("WebSocket connection initiated");
    }

    // Update subscription method
    private void subscribeToKline(WebSocket webSocket) {
        // Subscribe to OHLC data for all pairs
        String ohlcSubscription = """
            {
                "event": "subscribe",
                "pair": ["XBT/USD","ETH/USD","LTC/USD"],
                "subscription": {
                    "name": "ohlc",
                    "interval": 1
                }
            }
            """;
        webSocket.send(ohlcSubscription);

        // Subscribe to ticker for all pairs
        String tickerSubscription = """
            {
                "event": "subscribe",
                "pair": ["XBT/USD","ETH/USD","LTC/USD"],
                "subscription": {
                    "name": "ticker"
                }
            }
            """;
        webSocket.send(tickerSubscription);
        
        logger.info("Subscribed to market data for BTC, ETH and LTC pairs");
    }

    @PreDestroy
    public void shutdown() {
        if (webSocket != null) {
            webSocket.close(1000, "Service shutting down");
        }
        client.dispatcher().executorService().shutdown();
    }

    // Get all visible trading pairs
    public List<ExchangeCoin> findAllVisibleSymbols() {
        return exchangeCoinRepository.findAllVisible();
    }

    // Get coin summary data (thumbnail information)
    public List<CoinThumb> findAllSymbolThumb() {
        return exchangeCoinRepository.findAllThumbs();
    }

    // Get K-line historical data for a specific symbol within a time range and resolution
    public List<KLine> findKLineHistory(String symbol, long fromTime, long toTime, String period) {
        return kLineRepository.findBySymbolAndTimeBetweenAndPeriod(symbol, fromTime, toTime, period);
    }

    // Get the latest trade records for a specific symbol up to a certain size
    public List<ExchangeTrade> findLatestTrade(String symbol, int size) {
        Pageable pageable = PageRequest.of(0, size);
        return exchangeTradeRepository.findBySymbolOrderByTimeDesc(symbol, pageable);
    }

    // Calculate the total trading volume for a specific symbol within a time range
    public BigDecimal findTradeVolume(String symbol, long timeStart, long timeEnd) {
        return kLineRepository.calculateTotalVolume(symbol, timeStart, timeEnd);
    }
}