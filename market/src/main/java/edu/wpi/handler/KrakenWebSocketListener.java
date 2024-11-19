package edu.wpi.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.entities.ExchangeCoin;
import edu.wpi.entities.KLine;
import edu.wpi.repositories.ExchangeCoinRepository;
import edu.wpi.repositories.KLineRepository;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class KrakenWebSocketListener extends WebSocketListener {
    private static final Logger logger = LoggerFactory.getLogger(KrakenWebSocketListener.class);
    private final KLineRepository kLineRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeCoinRepository exchangeCoinRepository;

    public KrakenWebSocketListener(KLineRepository kLineRepository, ExchangeCoinRepository exchangeCoinRepository) {
        this.kLineRepository = kLineRepository;

        this.exchangeCoinRepository = exchangeCoinRepository;
    }


    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            JsonNode root = objectMapper.readTree(text);

            if (root.isArray() && root.size() >= 4) {
                JsonNode dataArray = root.get(1);
                String channelName = root.get(2).asText();
                String pair = root.get(3).asText();

                if ("ohlc-1".equals(channelName) && dataArray.size() >= 9) {
                    handleKLineData(dataArray, pair);
                } else if ("ticker".equals(channelName)) {
                    handleTickerData(dataArray, pair);
                }
            } else {
                logger.debug("Received non-data message: {}", text);
            }
        } catch (Exception e) {
            logger.error("Error processing Kraken message: ", e);
            logger.error("Raw message: {}", text);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        logger.info("Kraken WebSocket connection established");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        logger.error("Kraken WebSocket connection failed: ", t);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        logger.info("Kraken WebSocket connection closed: {} - {}", code, reason);
    }

    private void handleKLineData(JsonNode dataArray, String pair) {
        KLine kLine = KLine.builder().symbol(pair).time(Math.round(Double.parseDouble(dataArray.get(0).asText()))).openPrice(new BigDecimal(dataArray.get(2).asText())).highestPrice(new BigDecimal(dataArray.get(3).asText())).lowestPrice(new BigDecimal(dataArray.get(4).asText())).closePrice(new BigDecimal(dataArray.get(5).asText())).volume(new BigDecimal(dataArray.get(7).asText())).turnover(new BigDecimal(dataArray.get(6).asText())).count(dataArray.get(8).asInt()).period("1m").build();

        kLineRepository.save(kLine);
        logger.info("Saved KLine: {}", kLine);
    }

    private void handleTickerData(JsonNode tickerData, String pair) {
        try {
            ExchangeCoin coin = new ExchangeCoin();
            coin.setSymbol(pair);
            coin.setCoinSymbol(pair.split("/")[0]);
            coin.setBaseSymbol(pair.split("/")[1]);

            // Set basic fields
            coin.setEnable(1);
            coin.setVisible(1);
            coin.setExchangeable(1);

            // Map volume data
            if (tickerData.has("v")) {
                JsonNode volumeNode = tickerData.get("v");
                coin.setMinVolume(new BigDecimal(volumeNode.get(0).asText())); // Today's volume
                coin.setMaxVolume(new BigDecimal(volumeNode.get(1).asText())); // 24h volume
            }

            // Map price limits
            if (tickerData.has("l") && tickerData.has("h")) {
                coin.setMinSellPrice(new BigDecimal(tickerData.get("l").get(1).asText())); // 24h low
                coin.setMaxBuyPrice(new BigDecimal(tickerData.get("h").get(1).asText()));  // 24h high
            }

            exchangeCoinRepository.save(coin);
            logger.info("Saved ticker data for {}: {}", pair, coin);
        } catch (Exception e) {
            logger.error("Error processing ticker data for {}: {}", pair, e.getMessage());
        }
    }
}