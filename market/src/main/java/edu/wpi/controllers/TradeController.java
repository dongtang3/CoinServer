package edu.wpi.controllers;

import edu.wpi.entities.*;
import edu.wpi.repositories.TradeRepository;
import edu.wpi.repositories.WalletRepository;
import edu.wpi.services.MarketService;
import edu.wpi.services.TradeService;
import edu.wpi.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trades")
@Slf4j
public class TradeController {
    private final MarketService marketService;
    private final TradeService tradeService;
    private final TradeRepository tradeRepository;
    private final JwtUtil jwtUtil;

    private final WalletRepository walletRepository;
    public TradeController(MarketService marketService, TradeService tradeService, TradeRepository tradeRepository, JwtUtil jwtUtil, WalletRepository walletRepository) {
        this.marketService = marketService;
        this.tradeService = tradeService;
        this.tradeRepository = tradeRepository;
        this.jwtUtil = jwtUtil;
        this.walletRepository = walletRepository;
    }

    @PostMapping("/buy")
    public ResponseEntity<Trade> buyOrder(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TradeRequest tradeRequest) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserIdFromToken(token);
            String symbol = tradeRequest.getSymbol();
            BigDecimal amount = tradeRequest.getAmount();

            Trade trade = tradeService.executeTrade(
                    userId,
                    symbol,
                    amount,
                    marketService.getCurrentPrice(symbol),
                    ExchangeOrderDirection.BUY
            );
            return ResponseEntity.ok(trade);
        } catch (Exception e) {
            log.error("Buy order failed: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<Trade> sellOrder(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TradeRequest tradeRequest) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserIdFromToken(token);
            String symbol = tradeRequest.getSymbol();
            BigDecimal amount = tradeRequest.getAmount();

            Trade trade = tradeService.executeTrade(
                    userId,
                    symbol,
                    amount,
                    marketService.getCurrentPrice(symbol),
                    ExchangeOrderDirection.SELL
            );
            return ResponseEntity.ok(trade);
        } catch (Exception e) {
            log.error("Sell order failed: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Trade>> getUserTrades(@PathVariable String userId) {
//        return ResponseEntity.ok(tradeRepository.findByUserId(userId));
//    }

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

    @GetMapping("/user/trades")
    public ResponseEntity<List<Trade>> getTradeByUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserIdFromToken(token);
        return ResponseEntity.ok(tradeRepository.findByUserId(userId));
    }

    @GetMapping("/wallet/balance")
    public ResponseEntity<Optional<Wallet>> getWalletBalance(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUserIdFromToken(token);
            Optional<Wallet> wallet = walletRepository.findByUserId(userId); // Implement this service method
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            log.error("Failed to fetch wallet balance: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}