package edu.wpi.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.entities.KLine;
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

    public KrakenWebSocketListener(KLineRepository kLineRepository) {
        this.kLineRepository = kLineRepository;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            JsonNode root = objectMapper.readTree(text);

            if (root.isArray() && root.size() >= 4 && root.get(1).isArray()) {
                JsonNode dataArray = root.get(1);
                String channelName = root.get(2).asText();
                String pair = root.get(3).asText();

                if ("ohlc-1".equals(channelName) && dataArray.size() >= 9) {
                    KLine kLine = KLine.builder()
                            .symbol(pair)
                            .time(Math.round(Double.parseDouble(dataArray.get(0).asText())))
                            .openPrice(new BigDecimal(dataArray.get(2).asText()))
                            .highestPrice(new BigDecimal(dataArray.get(3).asText()))
                            .lowestPrice(new BigDecimal(dataArray.get(4).asText()))
                            .closePrice(new BigDecimal(dataArray.get(5).asText()))
                            .volume(new BigDecimal(dataArray.get(7).asText()))
                            .turnover(new BigDecimal(dataArray.get(6).asText())) // vwap
                            .count(dataArray.get(8).asInt())
                            .period("1m")
                            .build();

                    kLineRepository.save(kLine);
                    logger.info("Saved KLine: {}", kLine);
                }
            } else {
                logger.debug("Received non-OHLC message: {}", text);
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
}