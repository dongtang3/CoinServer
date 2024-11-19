package edu.wpi.controllers;

import edu.wpi.entities.ExchangeOrderDirection;
import edu.wpi.entities.Trade;
import edu.wpi.repositories.TradeRepository;
import edu.wpi.services.MarketService;
import edu.wpi.services.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/trades")
@Slf4j
public class TradeController {
    private final MarketService marketService;
    private final TradeService tradeService;
    private final TradeRepository tradeRepository;

    public TradeController(MarketService marketService, TradeService tradeService, TradeRepository tradeRepository) {
        this.marketService = marketService;
        this.tradeService = tradeService;
        this.tradeRepository = tradeRepository;
    }

    @PostMapping("/buy")
    public ResponseEntity<Trade> buyOrder(@RequestParam String userId,
                                          @RequestParam String symbol,
                                          @RequestParam BigDecimal amount) {
        try {
            Trade trade = tradeService.executeTrade(userId, symbol, amount,
                    marketService.getCurrentPrice(symbol), ExchangeOrderDirection.BUY);
            return ResponseEntity.ok(trade);
        } catch (Exception e) {
            log.error("Buy order failed: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<Trade> sellOrder(@RequestParam String userId,
                                         @RequestParam String symbol,
                                         @RequestParam BigDecimal amount) {
        try {
            Trade trade = tradeService.executeTrade(userId, symbol, amount,
                    marketService.getCurrentPrice(symbol), ExchangeOrderDirection.SELL);
            return ResponseEntity.ok(trade);
        } catch (Exception e) {
            log.error("Sell order failed: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trade>> getUserTrades(@PathVariable String userId) {
        return ResponseEntity.ok(tradeRepository.findByUserId(userId));
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<Trade>> getSymbolTrades(@PathVariable String symbol) {
        return ResponseEntity.ok(tradeRepository.findBySymbol(symbol));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Trade>> getTradeHistory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(tradeRepository.findByTimestampBetween(start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trade> getTradeById(@PathVariable Long id) {
        return tradeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}